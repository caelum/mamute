var Markdown;

if (typeof exports === "object" && typeof require === "function") // we're in a CommonJS (e.g. Node.js) module
    Markdown = exports;
else
    Markdown = {};
    
// The following text is included for historical reasons, but should
// be taken with a pinch of salt; it's not all true anymore.

//
// Wherever possible, Showdown is a straight, line-by-line port
// of the Perl version of Markdown.
//
// This is not a normal parser design; it's basically just a
// series of string substitutions.  It's hard to read and
// maintain this way,  but keeping Showdown close to the original
// design makes it easier to port new features.
//
// More importantly, Showdown behaves like markdown.pl in most
// edge cases.  So web applications can do client-side preview
// in Javascript, and then build identical HTML on the server.
//
// This port needs the new RegExp functionality of ECMA 262,
// 3rd Edition (i.e. Javascript 1.5).  Most modern web browsers
// should do fine.  Even with the new regular expression features,
// We do a lot of work to emulate Perl's regex functionality.
// The tricky changes in this file mostly have the "attacklab:"
// label.  Major or self-explanatory changes don't.
//
// Smart diff tools like Araxis Merge will be able to match up
// this file with markdown.pl in a useful way.  A little tweaking
// helps: in a copy of markdown.pl, replace "#" with "//" and
// replace "$text" with "text".  Be sure to ignore whitespace
// and line endings.
//


//
// Usage:
//
//   var text = "Markdown *rocks*.";
//
//   var converter = new Markdown.Converter();
//   var html = converter.makeHtml(text);
//
//   alert(html);
//
// Note: move the sample code to the bottom of this
// file before uncommenting it.
//

(function () {

    function identity(x) { return x; }
    function returnFalse(x) { return false; }

    function HookCollection() { }

    HookCollection.prototype = {

        chain: function (hookname, func) {
            var original = this[hookname];
            if (!original)
                throw new Error("unknown hook " + hookname);

            if (original === identity)
                this[hookname] = func;
            else
                this[hookname] = function (x) { return func(original(x)); }
        },
        set: function (hookname, func) {
            if (!this[hookname])
                throw new Error("unknown hook " + hookname);
            this[hookname] = func;
        },
        addNoop: function (hookname) {
            this[hookname] = identity;
        },
        addFalse: function (hookname) {
            this[hookname] = returnFalse;
        }
    };

    Markdown.HookCollection = HookCollection;

    // g_urls and g_titles allow arbitrary user-entered strings as keys. This
    // caused an exception (and hence stopped the rendering) when the user entered
    // e.g. [push] or [__proto__]. Adding a prefix to the actual key prevents this
    // (since no builtin property starts with "s_"). See
    // http://meta.stackoverflow.com/questions/64655/strange-wmd-bug
    // (granted, switching from Array() to Object() alone would have left only __proto__
    // to be a problem)
    function SaveHash() { }
    SaveHash.prototype = {
        set: function (key, value) {
            this["s_" + key] = value;
        },
        get: function (key) {
            return this["s_" + key];
        }
    };

    Markdown.Converter = function () {
        var pluginHooks = this.hooks = new HookCollection();
        pluginHooks.addNoop("plainLinkText");  // given a URL that was encountered by itself (without markup), should return the link text that's to be given to this link
        pluginHooks.addNoop("preConversion");  // called with the orignal text as given to makeHtml. The result of this plugin hook is the actual markdown source that will be cooked
        pluginHooks.addNoop("postConversion"); // called with the final cooked HTML code. The result of this plugin hook is the actual output of makeHtml

        //
        // Private state of the converter instance:
        //

        // Global hashes, used by various utility routines
        var g_urls;
        var g_titles;
        var g_html_blocks;

        // Used to track when we're inside an ordered or unordered list
        // (see _ProcessListItems() for details):
        var g_list_level;

        this.makeHtml = function (text) {

            //
            // Main function. The order in which other subs are called here is
            // essential. Link and image substitutions need to happen before
            // _EscapeSpecialCharsWithinTagAttributes(), so that any *'s or _'s in the <a>
            // and <img> tags get encoded.
            //

            // This will only happen if makeHtml on the same converter instance is called from a plugin hook.
            // Don't do that.
            if (g_urls)
                throw new Error("Recursive call to converter.makeHtml");
        
            // Create the private state objects.
            g_urls = new SaveHash();
            g_titles = new SaveHash();
            g_html_blocks = [];
            g_list_level = 0;

            text = pluginHooks.preConversion(text);

            // attacklab: Replace ~ with ~T
            // This lets us use tilde as an escape char to avoid md5 hashes
            // The choice of character is arbitray; anything that isn't
            // magic in Markdown will work.
            text = text.replace(/~/g, "~T");

            // attacklab: Replace $ with ~D
            // RegExp interprets $ as a special character
            // when it's in a replacement string
            text = text.replace(/\$/g, "~D");

            // Standardize line endings
            text = text.replace(/\r\n/g, "\n"); // DOS to Unix
            text = text.replace(/\r/g, "\n"); // Mac to Unix

            // Make sure text begins and ends with a couple of newlines:
            text = "\n\n" + text + "\n\n";

            // Convert all tabs to spaces.
            text = _Detab(text);

            // Strip any lines consisting only of spaces and tabs.
            // This makes subsequent regexen easier to write, because we can
            // match consecutive blank lines with /\n+/ instead of something
            // contorted like /[ \t]*\n+/ .
            text = text.replace(/^[ \t]+$/mg, "");

            // Turn block-level HTML blocks into hash entries
            text = _HashHTMLBlocks(text);

            // Strip link definitions, store in hashes.
            text = _StripLinkDefinitions(text);

            text = _RunBlockGamut(text);

            text = _UnescapeSpecialChars(text);

            // attacklab: Restore dollar signs
            text = text.replace(/~D/g, "$$");

            // attacklab: Restore tildes
            text = text.replace(/~T/g, "~");

            text = pluginHooks.postConversion(text);

            g_html_blocks = g_titles = g_urls = null;

            return text;
        };

        function _StripLinkDefinitions(text) {
            //
            // Strips link definitions from text, stores the URLs and titles in
            // hash references.
            //

            // Link defs are in the form: ^[id]: url "optional title"

            /*
            text = text.replace(/
                ^[ ]{0,3}\[(.+)\]:  // id = $1  attacklab: g_tab_width - 1
                [ \t]*
                \n?                 // maybe *one* newline
                [ \t]*
                <?(\S+?)>?          // url = $2
                (?=\s|$)            // lookahead for whitespace instead of the lookbehind removed below
                [ \t]*
                \n?                 // maybe one newline
                [ \t]*
                (                   // (potential) title = $3
                    (\n*)           // any lines skipped = $4 attacklab: lookbehind removed
                    [ \t]+
                    ["(]
                    (.+?)           // title = $5
                    [")]
                    [ \t]*
                )?                  // title is optional
                (?:\n+|$)
            /gm, function(){...});
            */

            text = text.replace(/^[ ]{0,3}\[(.+)\]:[ \t]*\n?[ \t]*<?(\S+?)>?(?=\s|$)[ \t]*\n?[ \t]*((\n*)["(](.+?)[")][ \t]*)?(?:\n+)/gm,
                function (wholeMatch, m1, m2, m3, m4, m5) {
                    m1 = m1.toLowerCase();
                    g_urls.set(m1, _EncodeAmpsAndAngles(m2));  // Link IDs are case-insensitive
                    if (m4) {
                        // Oops, found blank lines, so it's not a title.
                        // Put back the parenthetical statement we stole.
                        return m3;
                    } else if (m5) {
                        g_titles.set(m1, m5.replace(/"/g, "&quot;"));
                    }

                    // Completely remove the definition from the text
                    return "";
                }
            );

            return text;
        }

        function _HashHTMLBlocks(text) {

            // Hashify HTML blocks:
            // We only want to do this for block-level HTML tags, such as headers,
            // lists, and tables. That's because we still want to wrap <p>s around
            // "paragraphs" that are wrapped in non-block-level tags, such as anchors,
            // phrase emphasis, and spans. The list of tags we're looking for is
            // hard-coded:
            var block_tags_a = "p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math|ins|del"
            var block_tags_b = "p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math"

            // First, look for nested blocks, e.g.:
            //   <div>
            //     <div>
            //     tags for inner block must be indented.
            //     </div>
            //   </div>
            //
            // The outermost tags must start at the left margin for this to match, and
            // the inner nested divs must be indented.
            // We need to do this before the next, more liberal match, because the next
            // match will start at the first `<div>` and stop at the first `</div>`.

            // attacklab: This regex can be expensive when it fails.

            /*
            text = text.replace(/
                (                       // save in $1
                    ^                   // start of line  (with /m)
                    <($block_tags_a)    // start tag = $2
                    \b                  // word break
                                        // attacklab: hack around khtml/pcre bug...
                    [^\r]*?\n           // any number of lines, minimally matching
                    </\2>               // the matching end tag
                    [ \t]*              // trailing spaces/tabs
                    (?=\n+)             // followed by a newline
                )                       // attacklab: there are sentinel newlines at end of document
            /gm,function(){...}};
            */
            text = text.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math|ins|del)\b[^\r]*?\n<\/\2>[ \t]*(?=\n+))/gm, hashElement);

            //
            // Now match more liberally, simply from `\n<tag>` to `</tag>\n`
            //

            /*
            text = text.replace(/
                (                       // save in $1
                    ^                   // start of line  (with /m)
                    <($block_tags_b)    // start tag = $2
                    \b                  // word break
                                        // attacklab: hack around khtml/pcre bug...
                    [^\r]*?             // any number of lines, minimally matching
                    .*</\2>             // the matching end tag
                    [ \t]*              // trailing spaces/tabs
                    (?=\n+)             // followed by a newline
                )                       // attacklab: there are sentinel newlines at end of document
            /gm,function(){...}};
            */
            text = text.replace(/^(<(p|div|h[1-6]|blockquote|pre|table|dl|ol|ul|script|noscript|form|fieldset|iframe|math)\b[^\r]*?.*<\/\2>[ \t]*(?=\n+)\n)/gm, hashElement);

            // Special case just for <hr />. It was easier to make a special case than
            // to make the other regex more complicated.  

            /*
            text = text.replace(/
                \n                  // Starting after a blank line
                [ ]{0,3}
                (                   // save in $1
                    (<(hr)          // start tag = $2
                        \b          // word break
                        ([^<>])*?
                    \/?>)           // the matching end tag
                    [ \t]*
                    (?=\n{2,})      // followed by a blank line
                )
            /g,hashElement);
            */
            text = text.replace(/\n[ ]{0,3}((<(hr)\b([^<>])*?\/?>)[ \t]*(?=\n{2,}))/g, hashElement);

            // Special case for standalone HTML comments:

            /*
            text = text.replace(/
                \n\n                                            // Starting after a blank line
                [ ]{0,3}                                        // attacklab: g_tab_width - 1
                (                                               // save in $1
                    <!
                    (--(?:|(?:[^>-]|-[^>])(?:[^-]|-[^-])*)--)   // see http://www.w3.org/TR/html-markup/syntax.html#comments and http://meta.stackoverflow.com/q/95256
                    >
                    [ \t]*
                    (?=\n{2,})                                  // followed by a blank line
                )
            /g,hashElement);
            */
            text = text.replace(/\n\n[ ]{0,3}(<!(--(?:|(?:[^>-]|-[^>])(?:[^-]|-[^-])*)--)>[ \t]*(?=\n{2,}))/g, hashElement);

            // PHP and ASP-style processor instructions (<?...?> and <%...%>)

            /*
            text = text.replace(/
                (?:
                    \n\n            // Starting after a blank line
                )
                (                   // save in $1
                    [ ]{0,3}        // attacklab: g_tab_width - 1
                    (?:
                        <([?%])     // $2
                        [^\r]*?
                        \2>
                    )
                    [ \t]*
                    (?=\n{2,})      // followed by a blank line
                )
            /g,hashElement);
            */
            text = text.replace(/(?:\n\n)([ ]{0,3}(?:<([?%])[^\r]*?\2>)[ \t]*(?=\n{2,}))/g, hashElement);

            return text;
        }

        function hashElement(wholeMatch, m1) {
            var blockText = m1;

            // Undo double lines
            blockText = blockText.replace(/^\n+/, "");

            // strip trailing blank lines
            blockText = blockText.replace(/\n+$/g, "");

            // Replace the element text with a marker ("~KxK" where x is its key)
            blockText = "\n\n~K" + (g_html_blocks.push(blockText) - 1) + "K\n\n";

            return blockText;
        }

        function _RunBlockGamut(text, doNotUnhash) {
            //
            // These are all the transformations that form block-level
            // tags like paragraphs, headers, and list items.
            //
            text = _DoHeaders(text);

            // Do Horizontal Rules:
            var replacement = "<hr />\n";
            text = text.replace(/^[ ]{0,2}([ ]?\*[ ]?){3,}[ \t]*$/gm, replacement);
            text = text.replace(/^[ ]{0,2}([ ]?-[ ]?){3,}[ \t]*$/gm, replacement);
            text = text.replace(/^[ ]{0,2}([ ]?_[ ]?){3,}[ \t]*$/gm, replacement);

            text = _DoLists(text);
            text = _DoCodeBlocks(text);
            text = _DoBlockQuotes(text);

            // We already ran _HashHTMLBlocks() before, in Markdown(), but that
            // was to escape raw HTML in the original Markdown source. This time,
            // we're escaping the markup we've just created, so that we don't wrap
            // <p> tags around block-level tags.
            text = _HashHTMLBlocks(text);
            text = _FormParagraphs(text, doNotUnhash);

            return text;
        }

        function _RunSpanGamut(text) {
            //
            // These are all the transformations that occur *within* block-level
            // tags like paragraphs, headers, and list items.
            //

            text = _DoCodeSpans(text);
            text = _EscapeSpecialCharsWithinTagAttributes(text);
            text = _EncodeBackslashEscapes(text);

            // Process anchor and image tags. Images must come first,
            // because ![foo][f] looks like an anchor.
            text = _DoImages(text);
            text = _DoAnchors(text);

            // Make links out of things like `<http://example.com/>`
            // Must come after _DoAnchors(), because you can use < and >
            // delimiters in inline links like [this](<url>).
            text = _DoAutoLinks(text);
            
            text = text.replace(/~P/g, "://"); // put in place to prevent autolinking; reset now
            
            text = _EncodeAmpsAndAngles(text);
            text = _DoItalicsAndBold(text);

            // Do hard breaks:
            text = text.replace(/  +\n/g, " <br>\n");

            return text;
        }

        function _EscapeSpecialCharsWithinTagAttributes(text) {
            //
            // Within tags -- meaning between < and > -- encode [\ ` * _] so they
            // don't conflict with their use in Markdown for code, italics and strong.
            //

            // Build a regex to find HTML tags and comments.  See Friedl's 
            // "Mastering Regular Expressions", 2nd Ed., pp. 200-201.

            // SE: changed the comment part of the regex

            var regex = /(<[a-z\/!$]("[^"]*"|'[^']*'|[^'">])*>|<!(--(?:|(?:[^>-]|-[^>])(?:[^-]|-[^-])*)--)>)/gi;

            text = text.replace(regex, function (wholeMatch) {
                var tag = wholeMatch.replace(/(.)<\/?code>(?=.)/g, "$1`");
                tag = escapeCharacters(tag, wholeMatch.charAt(1) == "!" ? "\\`*_/" : "\\`*_"); // also escape slashes in comments to prevent autolinking there -- http://meta.stackoverflow.com/questions/95987
                return tag;
            });

            return text;
        }

        function _DoAnchors(text) {
            //
            // Turn Markdown link shortcuts into XHTML <a> tags.
            //
            //
            // First, handle reference-style links: [link text] [id]
            //

            /*
            text = text.replace(/
                (                           // wrap whole match in $1
                    \[
                    (
                        (?:
                            \[[^\]]*\]      // allow brackets nested one level
                            |
                            [^\[]           // or anything else
                        )*
                    )
                    \]

                    [ ]?                    // one optional space
                    (?:\n[ ]*)?             // one optional newline followed by spaces

                    \[
                    (.*?)                   // id = $3
                    \]
                )
                ()()()()                    // pad remaining backreferences
            /g, writeAnchorTag);
            */
            text = text.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g, writeAnchorTag);

            //
            // Next, inline-style links: [link text](url "optional title")
            //

            /*
            text = text.replace(/
                (                           // wrap whole match in $1
                    \[
                    (
                        (?:
                            \[[^\]]*\]      // allow brackets nested one level
                            |
                            [^\[\]]         // or anything else
                        )*
                    )
                    \]
                    \(                      // literal paren
                    [ \t]*
                    ()                      // no id, so leave $3 empty
                    <?(                     // href = $4
                        (?:
                            \([^)]*\)       // allow one level of (correctly nested) parens (think MSDN)
                            |
                            [^()\s]
                        )*?
                    )>?                
                    [ \t]*
                    (                       // $5
                        (['"])              // quote char = $6
                        (.*?)               // Title = $7
                        \6                  // matching quote
                        [ \t]*              // ignore any spaces/tabs between closing quote and )
                    )?                      // title is optional
                    \)
                )
            /g, writeAnchorTag);
            */

            text = text.replace(/(\[((?:\[[^\]]*\]|[^\[\]])*)\]\([ \t]*()<?((?:\([^)]*\)|[^()\s])*?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g, writeAnchorTag);

            //
            // Last, handle reference-style shortcuts: [link text]
            // These must come last in case you've also got [link test][1]
            // or [link test](/foo)
            //

            /*
            text = text.replace(/
                (                   // wrap whole match in $1
                    \[
                    ([^\[\]]+)      // link text = $2; can't contain '[' or ']'
                    \]
                )
                ()()()()()          // pad rest of backreferences
            /g, writeAnchorTag);
            */
            text = text.replace(/(\[([^\[\]]+)\])()()()()()/g, writeAnchorTag);

            return text;
        }

        function writeAnchorTag(wholeMatch, m1, m2, m3, m4, m5, m6, m7) {
            if (m7 == undefined) m7 = "";
            var whole_match = m1;
            var link_text = m2.replace(/:\/\//g, "~P"); // to prevent auto-linking withing the link. will be converted back after the auto-linker runs
            var link_id = m3.toLowerCase();
            var url = m4;
            var title = m7;

            if (url == "") {
                if (link_id == "") {
                    // lower-case and turn embedded newlines into spaces
                    link_id = link_text.toLowerCase().replace(/ ?\n/g, " ");
                }
                url = "#" + link_id;

                if (g_urls.get(link_id) != undefined) {
                    url = g_urls.get(link_id);
                    if (g_titles.get(link_id) != undefined) {
                        title = g_titles.get(link_id);
                    }
                }
                else {
                    if (whole_match.search(/\(\s*\)$/m) > -1) {
                        // Special case for explicit empty url
                        url = "";
                    } else {
                        return whole_match;
                    }
                }
            }
            url = encodeProblemUrlChars(url);
            url = escapeCharacters(url, "*_");
            var result = "<a href=\"" + url + "\"";

            if (title != "") {
                title = attributeEncode(title);
                title = escapeCharacters(title, "*_");
                result += " title=\"" + title + "\"";
            }

            result += ">" + link_text + "</a>";

            return result;
        }

        function _DoImages(text) {
            //
            // Turn Markdown image shortcuts into <img> tags.
            //

            //
            // First, handle reference-style labeled images: ![alt text][id]
            //

            /*
            text = text.replace(/
                (                   // wrap whole match in $1
                    !\[
                    (.*?)           // alt text = $2
                    \]

                    [ ]?            // one optional space
                    (?:\n[ ]*)?     // one optional newline followed by spaces

                    \[
                    (.*?)           // id = $3
                    \]
                )
                ()()()()            // pad rest of backreferences
            /g, writeImageTag);
            */
            text = text.replace(/(!\[(.*?)\][ ]?(?:\n[ ]*)?\[(.*?)\])()()()()/g, writeImageTag);

            //
            // Next, handle inline images:  ![alt text](url "optional title")
            // Don't forget: encode * and _

            /*
            text = text.replace(/
                (                   // wrap whole match in $1
                    !\[
                    (.*?)           // alt text = $2
                    \]
                    \s?             // One optional whitespace character
                    \(              // literal paren
                    [ \t]*
                    ()              // no id, so leave $3 empty
                    <?(\S+?)>?      // src url = $4
                    [ \t]*
                    (               // $5
                        (['"])      // quote char = $6
                        (.*?)       // title = $7
                        \6          // matching quote
                        [ \t]*
                    )?              // title is optional
                    \)
                )
            /g, writeImageTag);
            */
            text = text.replace(/(!\[(.*?)\]\s?\([ \t]*()<?(\S+?)>?[ \t]*((['"])(.*?)\6[ \t]*)?\))/g, writeImageTag);

            return text;
        }
        
        function attributeEncode(text) {
            // unconditionally replace angle brackets here -- what ends up in an attribute (e.g. alt or title)
            // never makes sense to have verbatim HTML in it (and the sanitizer would totally break it)
            return text.replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
        }

        function writeImageTag(wholeMatch, m1, m2, m3, m4, m5, m6, m7) {
            var whole_match = m1;
            var alt_text = m2;
            var link_id = m3.toLowerCase();
            var url = m4;
            var title = m7;

            if (!title) title = "";

            if (url == "") {
                if (link_id == "") {
                    // lower-case and turn embedded newlines into spaces
                    link_id = alt_text.toLowerCase().replace(/ ?\n/g, " ");
                }
                url = "#" + link_id;

                if (g_urls.get(link_id) != undefined) {
                    url = g_urls.get(link_id);
                    if (g_titles.get(link_id) != undefined) {
                        title = g_titles.get(link_id);
                    }
                }
                else {
                    return whole_match;
                }
            }
            
            alt_text = escapeCharacters(attributeEncode(alt_text), "*_[]()");
            url = escapeCharacters(url, "*_");
            var result = "<img src=\"" + url + "\" alt=\"" + alt_text + "\"";

            // attacklab: Markdown.pl adds empty title attributes to images.
            // Replicate this bug.

            //if (title != "") {
            title = attributeEncode(title);
            title = escapeCharacters(title, "*_");
            result += " title=\"" + title + "\"";
            //}

            result += " />";

            return result;
        }

        function _DoHeaders(text) {

            // Setext-style headers:
            //  Header 1
            //  ========
            //  
            //  Header 2
            //  --------
            //
            text = text.replace(/^(.+)[ \t]*\n=+[ \t]*\n+/gm,
                function (wholeMatch, m1) { return "<h1>" + _RunSpanGamut(m1) + "</h1>\n\n"; }
            );

            text = text.replace(/^(.+)[ \t]*\n-+[ \t]*\n+/gm,
                function (matchFound, m1) { return "<h2>" + _RunSpanGamut(m1) + "</h2>\n\n"; }
            );

            // atx-style headers:
            //  # Header 1
            //  ## Header 2
            //  ## Header 2 with closing hashes ##
            //  ...
            //  ###### Header 6
            //

            /*
            text = text.replace(/
                ^(\#{1,6})      // $1 = string of #'s
                [ \t]*
                (.+?)           // $2 = Header text
                [ \t]*
                \#*             // optional closing #'s (not counted)
                \n+
            /gm, function() {...});
            */

            text = text.replace(/^(\#{1,6})[ \t]*(.+?)[ \t]*\#*\n+/gm,
                function (wholeMatch, m1, m2) {
                    var h_level = m1.length;
                    return "<h" + h_level + ">" + _RunSpanGamut(m2) + "</h" + h_level + ">\n\n";
                }
            );

            return text;
        }

        function _DoLists(text) {
            //
            // Form HTML ordered (numbered) and unordered (bulleted) lists.
            //

            // attacklab: add sentinel to hack around khtml/safari bug:
            // http://bugs.webkit.org/show_bug.cgi?id=11231
            text += "~0";

            // Re-usable pattern to match any entirel ul or ol list:

            /*
            var whole_list = /
                (                                   // $1 = whole list
                    (                               // $2
                        [ ]{0,3}                    // attacklab: g_tab_width - 1
                        ([*+-]|\d+[.])              // $3 = first list item marker
                        [ \t]+
                    )
                    [^\r]+?
                    (                               // $4
                        ~0                          // sentinel for workaround; should be $
                        |
                        \n{2,}
                        (?=\S)
                        (?!                         // Negative lookahead for another list item marker
                            [ \t]*
                            (?:[*+-]|\d+[.])[ \t]+
                        )
                    )
                )
            /g
            */
            var whole_list = /^(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/gm;

            if (g_list_level) {
                text = text.replace(whole_list, function (wholeMatch, m1, m2) {
                    var list = m1;
                    var list_type = (m2.search(/[*+-]/g) > -1) ? "ul" : "ol";

                    var result = _ProcessListItems(list, list_type);

                    // Trim any trailing whitespace, to put the closing `</$list_type>`
                    // up on the preceding line, to get it past the current stupid
                    // HTML block parser. This is a hack to work around the terrible
                    // hack that is the HTML block parser.
                    result = result.replace(/\s+$/, "");
                    result = "<" + list_type + ">" + result + "</" + list_type + ">\n";
                    return result;
                });
            } else {
                whole_list = /(\n\n|^\n?)(([ ]{0,3}([*+-]|\d+[.])[ \t]+)[^\r]+?(~0|\n{2,}(?=\S)(?![ \t]*(?:[*+-]|\d+[.])[ \t]+)))/g;
                text = text.replace(whole_list, function (wholeMatch, m1, m2, m3) {
                    var runup = m1;
                    var list = m2;

                    var list_type = (m3.search(/[*+-]/g) > -1) ? "ul" : "ol";
                    var result = _ProcessListItems(list, list_type);
                    result = runup + "<" + list_type + ">\n" + result + "</" + list_type + ">\n";
                    return result;
                });
            }

            // attacklab: strip sentinel
            text = text.replace(/~0/, "");

            return text;
        }

        var _listItemMarkers = { ol: "\\d+[.]", ul: "[*+-]" };

        function _ProcessListItems(list_str, list_type) {
            //
            //  Process the contents of a single ordered or unordered list, splitting it
            //  into individual list items.
            //
            //  list_type is either "ul" or "ol".

            // The $g_list_level global keeps track of when we're inside a list.
            // Each time we enter a list, we increment it; when we leave a list,
            // we decrement. If it's zero, we're not in a list anymore.
            //
            // We do this because when we're not inside a list, we want to treat
            // something like this:
            //
            //    I recommend upgrading to version
            //    8. Oops, now this line is treated
            //    as a sub-list.
            //
            // As a single paragraph, despite the fact that the second line starts
            // with a digit-period-space sequence.
            //
            // Whereas when we're inside a list (or sub-list), that line will be
            // treated as the start of a sub-list. What a kludge, huh? This is
            // an aspect of Markdown's syntax that's hard to parse perfectly
            // without resorting to mind-reading. Perhaps the solution is to
            // change the syntax rules such that sub-lists must start with a
            // starting cardinal number; e.g. "1." or "a.".

            g_list_level++;

            // trim trailing blank lines:
            list_str = list_str.replace(/\n{2,}$/, "\n");

            // attacklab: add sentinel to emulate \z
            list_str += "~0";

            // In the original attacklab showdown, list_type was not given to this function, and anything
            // that matched /[*+-]|\d+[.]/ would just create the next <li>, causing this mismatch:
            //
            //  Markdown          rendered by WMD        rendered by MarkdownSharp
            //  ------------------------------------------------------------------
            //  1. first          1. first               1. first
            //  2. second         2. second              2. second
            //  - third           3. third                   * third
            //
            // We changed this to behave identical to MarkdownSharp. This is the constructed RegEx,
            // with {MARKER} being one of \d+[.] or [*+-], depending on list_type:
        
            /*
            list_str = list_str.replace(/
                (^[ \t]*)                       // leading whitespace = $1
                ({MARKER}) [ \t]+               // list marker = $2
                ([^\r]+?                        // list item text   = $3
                    (\n+)
                )
                (?=
                    (~0 | \2 ({MARKER}) [ \t]+)
                )
            /gm, function(){...});
            */

            var marker = _listItemMarkers[list_type];
            var re = new RegExp("(^[ \\t]*)(" + marker + ")[ \\t]+([^\\r]+?(\\n+))(?=(~0|\\1(" + marker + ")[ \\t]+))", "gm");
            var last_item_had_a_double_newline = false;
            list_str = list_str.replace(re,
                function (wholeMatch, m1, m2, m3) {
                    var item = m3;
                    var leading_space = m1;
                    var ends_with_double_newline = /\n\n$/.test(item);
                    var contains_double_newline = ends_with_double_newline || item.search(/\n{2,}/) > -1;

                    if (contains_double_newline || last_item_had_a_double_newline) {
                        item = _RunBlockGamut(_Outdent(item), /* doNotUnhash = */true);
                    }
                    else {
                        // Recursion for sub-lists:
                        item = _DoLists(_Outdent(item));
                        item = item.replace(/\n$/, ""); // chomp(item)
                        item = _RunSpanGamut(item);
                    }
                    last_item_had_a_double_newline = ends_with_double_newline;
                    return "<li>" + item + "</li>\n";
                }
            );

            // attacklab: strip sentinel
            list_str = list_str.replace(/~0/g, "");

            g_list_level--;
            return list_str;
        }

        function _DoCodeBlocks(text) {
            //
            //  Process Markdown `<pre><code>` blocks.
            //  

            /*
            text = text.replace(/
                (?:\n\n|^)
                (                               // $1 = the code block -- one or more lines, starting with a space/tab
                    (?:
                        (?:[ ]{4}|\t)           // Lines must start with a tab or a tab-width of spaces - attacklab: g_tab_width
                        .*\n+
                    )+
                )
                (\n*[ ]{0,3}[^ \t\n]|(?=~0))    // attacklab: g_tab_width
            /g ,function(){...});
            */

            // attacklab: sentinel workarounds for lack of \A and \Z, safari\khtml bug
            text += "~0";

            text = text.replace(/(?:\n\n|^)((?:(?:[ ]{4}|\t).*\n+)+)(\n*[ ]{0,3}[^ \t\n]|(?=~0))/g,
                function (wholeMatch, m1, m2) {
                    var codeblock = m1;
                    var nextChar = m2;

                    codeblock = _EncodeCode(_Outdent(codeblock));
                    codeblock = _Detab(codeblock);
                    codeblock = codeblock.replace(/^\n+/g, ""); // trim leading newlines
                    codeblock = codeblock.replace(/\n+$/g, ""); // trim trailing whitespace

                    codeblock = "<pre class='prettyprint'>" + codeblock + "\n</pre>";

                    return "\n\n" + codeblock + "\n\n" + nextChar;
                }
            );

            // attacklab: strip sentinel
            text = text.replace(/~0/, "");

            return text;
        }

        function hashBlock(text) {
            text = text.replace(/(^\n+|\n+$)/g, "");
            return "\n\n~K" + (g_html_blocks.push(text) - 1) + "K\n\n";
        }

        function _DoCodeSpans(text) {
            //
            // * Backtick quotes are used for <code></code> spans.
            // 
            // * You can use multiple backticks as the delimiters if you want to
            //   include literal backticks in the code span. So, this input:
            //     
            //      Just type ``foo `bar` baz`` at the prompt.
            //     
            //   Will translate to:
            //     
            //      <p>Just type <code>foo `bar` baz</code> at the prompt.</p>
            //     
            //   There's no arbitrary limit to the number of backticks you
            //   can use as delimters. If you need three consecutive backticks
            //   in your code, use four for delimiters, etc.
            //
            // * You can use spaces to get literal backticks at the edges:
            //     
            //      ... type `` `bar` `` ...
            //     
            //   Turns to:
            //     
            //      ... type <code>`bar`</code> ...
            //

            /*
            text = text.replace(/
                (^|[^\\])       // Character before opening ` can't be a backslash
                (`+)            // $2 = Opening run of `
                (               // $3 = The code block
                    [^\r]*?
                    [^`]        // attacklab: work around lack of lookbehind
                )
                \2              // Matching closer
                (?!`)
            /gm, function(){...});
            */

            text = text.replace(/(^|[^\\])(`+)([^\r]*?[^`])\2(?!`)/gm,
                function (wholeMatch, m1, m2, m3, m4) {
                    var c = m3;
                    c = c.replace(/^([ \t]*)/g, ""); // leading whitespace
                    c = c.replace(/[ \t]*$/g, ""); // trailing whitespace
                    c = _EncodeCode(c);
                    c = c.replace(/:\/\//g, "~P"); // to prevent auto-linking. Not necessary in code *blocks*, but in code spans. Will be converted back after the auto-linker runs.
                    return m1 + "<code>" + c + "</code>";
                }
            );

            return text;
        }

        function _EncodeCode(text) {
            //
            // Encode/escape certain characters inside Markdown code runs.
            // The point is that in code, these characters are literals,
            // and lose their special Markdown meanings.
            //
            // Encode all ampersands; HTML entities are not
            // entities within a Markdown code span.
            text = text.replace(/&/g, "&amp;");

            // Do the angle bracket song and dance:
            text = text.replace(/</g, "&lt;");
            text = text.replace(/>/g, "&gt;");

            // Now, escape characters that are magic in Markdown:
            text = escapeCharacters(text, "\*_{}[]\\", false);

            // jj the line above breaks this:
            //---

            //* Item

            //   1. Subitem

            //            special char: *
            //---

            return text;
        }

        function _DoItalicsAndBold(text) {

            // <strong> must go first:
            text = text.replace(/([\W_]|^)(\*\*|__)(?=\S)([^\r]*?\S[\*_]*)\2([\W_]|$)/g,
            "$1<strong>$3</strong>$4");

            text = text.replace(/([\W_]|^)(\*|_)(?=\S)([^\r\*_]*?\S)\2([\W_]|$)/g,
            "$1<em>$3</em>$4");

            return text;
        }

        function _DoBlockQuotes(text) {

            /*
            text = text.replace(/
                (                           // Wrap whole match in $1
                    (
                        ^[ \t]*>[ \t]?      // '>' at the start of a line
                        .+\n                // rest of the first line
                        (.+\n)*             // subsequent consecutive lines
                        \n*                 // blanks
                    )+
                )
            /gm, function(){...});
            */

            text = text.replace(/((^[ \t]*>[ \t]?.+\n(.+\n)*\n*)+)/gm,
                function (wholeMatch, m1) {
                    var bq = m1;

                    // attacklab: hack around Konqueror 3.5.4 bug:
                    // "----------bug".replace(/^-/g,"") == "bug"

                    bq = bq.replace(/^[ \t]*>[ \t]?/gm, "~0"); // trim one level of quoting

                    // attacklab: clean up hack
                    bq = bq.replace(/~0/g, "");

                    bq = bq.replace(/^[ \t]+$/gm, "");     // trim whitespace-only lines
                    bq = _RunBlockGamut(bq);             // recurse

                    bq = bq.replace(/(^|\n)/g, "$1  ");
                    // These leading spaces screw with <pre> content, so we need to fix that:
                    bq = bq.replace(
                            /(\s*<pre>[^\r]+?<\/pre>)/gm,
                        function (wholeMatch, m1) {
                            var pre = m1;
                            // attacklab: hack around Konqueror 3.5.4 bug:
                            pre = pre.replace(/^  /mg, "~0");
                            pre = pre.replace(/~0/g, "");
                            return pre;
                        });

                    return hashBlock("<blockquote>\n" + bq + "\n</blockquote>");
                }
            );
            return text;
        }

        function _FormParagraphs(text, doNotUnhash) {
            //
            //  Params:
            //    $text - string to process with html <p> tags
            //

            // Strip leading and trailing lines:
            text = text.replace(/^\n+/g, "");
            text = text.replace(/\n+$/g, "");

            var grafs = text.split(/\n{2,}/g);
            var grafsOut = [];
            
            var markerRe = /~K(\d+)K/;

            //
            // Wrap <p> tags.
            //
            var end = grafs.length;
            for (var i = 0; i < end; i++) {
                var str = grafs[i];

                // if this is an HTML marker, copy it
                if (markerRe.test(str)) {
                    grafsOut.push(str);
                }
                else if (/\S/.test(str)) {
                    str = _RunSpanGamut(str);
                    str = str.replace(/^([ \t]*)/g, "<p>");
                    str += "</p>"
                    grafsOut.push(str);
                }

            }
            //
            // Unhashify HTML blocks
            //
            if (!doNotUnhash) {
                end = grafsOut.length;
                for (var i = 0; i < end; i++) {
                    var foundAny = true;
                    while (foundAny) { // we may need several runs, since the data may be nested
                        foundAny = false;
                        grafsOut[i] = grafsOut[i].replace(/~K(\d+)K/g, function (wholeMatch, id) {
                            foundAny = true;
                            return g_html_blocks[id];
                        });
                    }
                }
            }
            return grafsOut.join("\n\n");
        }

        function _EncodeAmpsAndAngles(text) {
            // Smart processing for ampersands and angle brackets that need to be encoded.

            // Ampersand-encoding based entirely on Nat Irons's Amputator MT plugin:
            //   http://bumppo.net/projects/amputator/
            text = text.replace(/&(?!#?[xX]?(?:[0-9a-fA-F]+|\w+);)/g, "&amp;");

            // Encode naked <'s
            text = text.replace(/<(?![a-z\/?!]|~D)/gi, "&lt;");

            return text;
        }

        function _EncodeBackslashEscapes(text) {
            //
            //   Parameter:  String.
            //   Returns:    The string, with after processing the following backslash
            //               escape sequences.
            //

            // attacklab: The polite way to do this is with the new
            // escapeCharacters() function:
            //
            //     text = escapeCharacters(text,"\\",true);
            //     text = escapeCharacters(text,"`*_{}[]()>#+-.!",true);
            //
            // ...but we're sidestepping its use of the (slow) RegExp constructor
            // as an optimization for Firefox.  This function gets called a LOT.

            text = text.replace(/\\(\\)/g, escapeCharacters_callback);
            text = text.replace(/\\([`*_{}\[\]()>#+-.!])/g, escapeCharacters_callback);
            return text;
        }
        
        function handleTrailingParens(wholeMatch, lookbehind, protocol, link) {
            if (lookbehind)
                return wholeMatch;
            if (link.charAt(link.length - 1) !== ")")
                return "<" + protocol + link + ">";
            var parens = link.match(/[()]/g);
            var level = 0;
            for (var i = 0; i < parens.length; i++) {
                if (parens[i] === "(") {
                    if (level <= 0)
                        level = 1;
                    else
                        level++;
                }
                else {
                    level--;
                }
            }
            var tail = "";
            if (level < 0) {
                var re = new RegExp("\\){1," + (-level) + "}$");
                link = link.replace(re, function (trailingParens) {
                    tail = trailingParens;
                    return "";
                });
            }
            
            return "<" + protocol + link + ">" + tail;
        }

        function _DoAutoLinks(text) {

            // note that at this point, all other URL in the text are already hyperlinked as <a href=""></a>
            // *except* for the <http://www.foo.com> case

            // automatically add < and > around unadorned raw hyperlinks
            // must be preceded by a non-word character (and not by =" or <) and followed by non-word/EOF character
            // simulating the lookbehind in a consuming way is okay here, since a URL can neither and with a " nor
            // with a <, so there is no risk of overlapping matches.
            text = text.replace(/(="|<)?\b(https?|ftp)(:\/\/[-A-Z0-9+&@#\/%?=~_|\[\]\(\)!:,\.;]*[-A-Z0-9+&@#\/%=~_|\[\])])(?=$|\W)/gi, handleTrailingParens);

            //  autolink anything like <http://example.com>
            
            var replacer = function (wholematch, m1) { return "<a href=\"" + m1 + "\">" + pluginHooks.plainLinkText(m1) + "</a>"; }
            text = text.replace(/<((https?|ftp):[^'">\s]+)>/gi, replacer);

            // Email addresses: <address@domain.foo>
            /*
            text = text.replace(/
                <
                (?:mailto:)?
                (
                    [-.\w]+
                    \@
                    [-a-z0-9]+(\.[-a-z0-9]+)*\.[a-z]+
                )
                >
            /gi, _DoAutoLinks_callback());
            */

            /* disabling email autolinking, since we don't do that on the server, either
            text = text.replace(/<(?:mailto:)?([-.\w]+\@[-a-z0-9]+(\.[-a-z0-9]+)*\.[a-z]+)>/gi,
                function(wholeMatch,m1) {
                    return _EncodeEmailAddress( _UnescapeSpecialChars(m1) );
                }
            );
            */
            return text;
        }

        function _UnescapeSpecialChars(text) {
            //
            // Swap back in all the special characters we've hidden.
            //
            text = text.replace(/~E(\d+)E/g,
                function (wholeMatch, m1) {
                    var charCodeToReplace = parseInt(m1);
                    return String.fromCharCode(charCodeToReplace);
                }
            );
            return text;
        }

        function _Outdent(text) {
            //
            // Remove one level of line-leading tabs or spaces
            //

            // attacklab: hack around Konqueror 3.5.4 bug:
            // "----------bug".replace(/^-/g,"") == "bug"

            text = text.replace(/^(\t|[ ]{1,4})/gm, "~0"); // attacklab: g_tab_width

            // attacklab: clean up hack
            text = text.replace(/~0/g, "")

            return text;
        }

        function _Detab(text) {
            if (!/\t/.test(text))
                return text;

            var spaces = ["    ", "   ", "  ", " "],
            skew = 0,
            v;

            return text.replace(/[\n\t]/g, function (match, offset) {
                if (match === "\n") {
                    skew = offset + 1;
                    return match;
                }
                v = (offset - skew) % 4;
                skew = offset + 1;
                return spaces[v];
            });
        }

        //
        //  attacklab: Utility functions
        //

        var _problemUrlChars = /(?:["'*()[\]:]|~D)/g;

        // hex-encodes some unusual "problem" chars in URLs to avoid URL detection problems 
        function encodeProblemUrlChars(url) {
            if (!url)
                return "";

            var len = url.length;

            return url.replace(_problemUrlChars, function (match, offset) {
                if (match == "~D") // escape for dollar
                    return "%24";
                if (match == ":") {
                    if (offset == len - 1 || /[0-9\/]/.test(url.charAt(offset + 1)))
                        return ":"
                }
                return "%" + match.charCodeAt(0).toString(16);
            });
        }


        function escapeCharacters(text, charsToEscape, afterBackslash) {
            // First we have to escape the escape characters so that
            // we can build a character class out of them
            var regexString = "([" + charsToEscape.replace(/([\[\]\\])/g, "\\$1") + "])";

            if (afterBackslash) {
                regexString = "\\\\" + regexString;
            }

            var regex = new RegExp(regexString, "g");
            text = text.replace(regex, escapeCharacters_callback);

            return text;
        }


        function escapeCharacters_callback(wholeMatch, m1) {
            var charCodeToEscape = m1.charCodeAt(0);
            return "~E" + charCodeToEscape + "E";
        }

    }; // end of the Markdown.Converter constructor

})();
(function () {
    var output, Converter;
    if (typeof exports === "object" && typeof require === "function") { // we're in a CommonJS (e.g. Node.js) module
        output = exports;
        Converter = require("./Markdown.Converter").Converter;
    } else {
        output = window.Markdown;
        Converter = output.Converter;
    }
        
    output.getSanitizingConverter = function () {
        var converter = new Converter();
        converter.hooks.chain("postConversion", sanitizeHtml);
        converter.hooks.chain("postConversion", balanceTags);
        return converter;
    }

    function sanitizeHtml(html) {
        return html.replace(/<[^>]*>?/gi, sanitizeTag);
    }

    // (tags that can be opened/closed) | (tags that stand alone)
    var basic_tag_whitelist = /^(<\/?(b|blockquote|code|del|dd|dl|dt|em|h1|h2|h3|i|kbd|li|ol|p|pre|s|sup|sub|strong|strike|ul)>|<(br|hr)\s?\/?>)$/i;
    // <a href="url..." optional title>|</a>
    var a_white = /^(<a\shref="((https?|ftp):\/\/|\/)[-A-Za-z0-9+&@#\/%?=~_|!:,.;\(\)]+"(\stitle="[^"<>]+")?\s?>|<\/a>)$/i;

    // <img src="url..." optional width  optional height  optional alt  optional title
    var img_white = /^(<img\ssrc="(https?:\/\/|\/)[-A-Za-z0-9+&@#\/%?=~_|!:,.;\(\)]+"(\swidth="\d{1,3}")?(\sheight="\d{1,3}")?(\salt="[^"<>]*")?(\stitle="[^"<>]*")?\s?\/?>)$/i;

    function sanitizeTag(tag) {
        if (tag.match(basic_tag_whitelist) || tag.match(a_white) || tag.match(img_white))
            return tag;
        else
            return "";
    }

    /// <summary>
    /// attempt to balance HTML tags in the html string
    /// by removing any unmatched opening or closing tags
    /// IMPORTANT: we *assume* HTML has *already* been 
    /// sanitized and is safe/sane before balancing!
    /// 
    /// adapted from CODESNIPPET: A8591DBA-D1D3-11DE-947C-BA5556D89593
    /// </summary>
    function balanceTags(html) {

        if (html == "")
            return "";

        var re = /<\/?\w+[^>]*(\s|$|>)/g;
        // convert everything to lower case; this makes
        // our case insensitive comparisons easier
        var tags = html.toLowerCase().match(re);

        // no HTML tags present? nothing to do; exit now
        var tagcount = (tags || []).length;
        if (tagcount == 0)
            return html;

        var tagname, tag;
        var ignoredtags = "<p><img><br><li><hr>";
        var match;
        var tagpaired = [];
        var tagremove = [];
        var needsRemoval = false;

        // loop through matched tags in forward order
        for (var ctag = 0; ctag < tagcount; ctag++) {
            tagname = tags[ctag].replace(/<\/?(\w+).*/, "$1");
            // skip any already paired tags
            // and skip tags in our ignore list; assume they're self-closed
            if (tagpaired[ctag] || ignoredtags.search("<" + tagname + ">") > -1)
                continue;

            tag = tags[ctag];
            match = -1;

            if (!/^<\//.test(tag)) {
                // this is an opening tag
                // search forwards (next tags), look for closing tags
                for (var ntag = ctag + 1; ntag < tagcount; ntag++) {
                    if (!tagpaired[ntag] && tags[ntag] == "</" + tagname + ">") {
                        match = ntag;
                        break;
                    }
                }
            }

            if (match == -1)
                needsRemoval = tagremove[ctag] = true; // mark for removal
            else
                tagpaired[match] = true; // mark paired
        }

        if (!needsRemoval)
            return html;

        // delete all orphaned tags from the string

        var ctag = 0;
        html = html.replace(re, function (match) {
            var res = tagremove[ctag] ? "" : match;
            ctag++;
            return res;
        });
        return html;
    }
})();
﻿// needs Markdown.Converter.js at the moment

(function () {

    var util = {},
        position = {},
        ui = {},
        doc = window.document,
        re = window.RegExp,
        nav = window.navigator,
        SETTINGS = { lineLength: 72 },

    // Used to work around some browser bugs where we can't use feature testing.
        uaSniffed = {
            isIE: /msie/.test(nav.userAgent.toLowerCase()),
            isIE_5or6: /msie 6/.test(nav.userAgent.toLowerCase()) || /msie 5/.test(nav.userAgent.toLowerCase()),
            isOpera: /opera/.test(nav.userAgent.toLowerCase())
        };

    var defaultsStrings = {
        bold: "Strong <strong> Ctrl+B",
        boldexample: "strong text",

        italic: "Emphasis <em> Ctrl+I",
        italicexample: "emphasized text",

        link: "Hyperlink <a> Ctrl+L",
        linkdescription: "enter link description here",
        linkdialog: "<p><b>Insert Hyperlink</b></p><p>http://example.com/ \"optional title\"</p>",

        quote: "Blockquote <blockquote> Ctrl+Q",
        quoteexample: "Blockquote",

        code: "Code Sample <pre><code> Ctrl+K",
        codeexample: "enter code here",

        image: "Image <img> Ctrl+G",
        imagedescription: "enter image description here",
        imagedialog: "<p><b>Insert Image</b></p><p>http://example.com/images/diagram.jpg \"optional title\"<br><br>Need <a href='http://www.google.com/search?q=free+image+hosting' target='_blank'>free image hosting?</a></p>",

        olist: "Numbered List <ol> Ctrl+O",
        ulist: "Bulleted List <ul> Ctrl+U",
        litem: "List item",

        heading: "Heading <h1>/<h2> Ctrl+H",
        headingexample: "Heading",

        hr: "Horizontal Rule <hr> Ctrl+R",

        undo: "Undo - Ctrl+Z",
        redo: "Redo - Ctrl+Y",
        redomac: "Redo - Ctrl+Shift+Z",

        help: "Markdown Editing Help"
    };


    // -------------------------------------------------------------------
    //  YOUR CHANGES GO HERE
    //
    // I've tried to localize the things you are likely to change to
    // this area.
    // -------------------------------------------------------------------

    // The default text that appears in the dialog input box when entering
    // links.
    var imageDefaultText = "http://";
    var linkDefaultText = "http://";

    // -------------------------------------------------------------------
    //  END OF YOUR CHANGES
    // -------------------------------------------------------------------

    // options, if given, can have the following properties:
    //   options.helpButton = { handler: yourEventHandler }
    //   options.strings = { italicexample: "slanted text" }
    // `yourEventHandler` is the click handler for the help button.
    // If `options.helpButton` isn't given, not help button is created.
    // `options.strings` can have any or all of the same properties as
    // `defaultStrings` above, so you can just override some string displayed
    // to the user on a case-by-case basis, or translate all strings to
    // a different language.
    //
    // For backwards compatibility reasons, the `options` argument can also
    // be just the `helpButton` object, and `strings.help` can also be set via
    // `helpButton.title`. This should be considered legacy.
    //
    // The constructed editor object has the methods:
    // - getConverter() returns the markdown converter object that was passed to the constructor
    // - run() actually starts the editor; should be called after all necessary plugins are registered. Calling this more than once is a no-op.
    // - refreshPreview() forces the preview to be updated. This method is only available after run() was called.
    Markdown.Editor = function (markdownConverter, idPostfix, options) {
        
        options = options || {};

        if (typeof options.handler === "function") { //backwards compatible behavior
            options = { helpButton: options };
        }
        options.strings = options.strings || {};
        if (options.helpButton) {
            options.strings.help = options.strings.help || options.helpButton.title;
        }
        var getString = function (identifier) { return options.strings[identifier] || defaultsStrings[identifier]; }

        idPostfix = idPostfix || "";

        var hooks = this.hooks = new Markdown.HookCollection();
        hooks.addNoop("onPreviewRefresh");       // called with no arguments after the preview has been refreshed
        hooks.addNoop("postBlockquoteCreation"); // called with the user's selection *after* the blockquote was created; should return the actual to-be-inserted text
        hooks.addFalse("insertImageDialog");     /* called with one parameter: a callback to be called with the URL of the image. If the application creates
                                                  * its own image insertion dialog, this hook should return true, and the callback should be called with the chosen
                                                  * image url (or null if the user cancelled). If this hook returns false, the default dialog will be used.
                                                  */

        this.getConverter = function () { return markdownConverter; }

        var that = this,
            panels;

        this.run = function () {
            if (panels)
                return; // already initialized

            panels = new PanelCollection(idPostfix);
            var commandManager = new CommandManager(hooks, getString);
            var previewManager = new PreviewManager(markdownConverter, panels, function () { hooks.onPreviewRefresh(); });
            var undoManager, uiManager;

            if (!/\?noundo/.test(doc.location.href)) {
                undoManager = new UndoManager(function () {
                    previewManager.refresh();
                    if (uiManager) // not available on the first call
                        uiManager.setUndoRedoButtonStates();
                }, panels);
                this.textOperation = function (f) {
                    undoManager.setCommandMode();
                    f();
                    that.refreshPreview();
                }
            }

            uiManager = new UIManager(idPostfix, panels, undoManager, previewManager, commandManager, options.helpButton, getString);
            uiManager.setUndoRedoButtonStates();

            var forceRefresh = that.refreshPreview = function () { previewManager.refresh(true); };

            forceRefresh();
        };

    }

    // before: contains all the text in the input box BEFORE the selection.
    // after: contains all the text in the input box AFTER the selection.
    function Chunks() { }

    // startRegex: a regular expression to find the start tag
    // endRegex: a regular expresssion to find the end tag
    Chunks.prototype.findTags = function (startRegex, endRegex) {

        var chunkObj = this;
        var regex;

        if (startRegex) {

            regex = util.extendRegExp(startRegex, "", "$");

            this.before = this.before.replace(regex,
                function (match) {
                    chunkObj.startTag = chunkObj.startTag + match;
                    return "";
                });

            regex = util.extendRegExp(startRegex, "^", "");

            this.selection = this.selection.replace(regex,
                function (match) {
                    chunkObj.startTag = chunkObj.startTag + match;
                    return "";
                });
        }

        if (endRegex) {

            regex = util.extendRegExp(endRegex, "", "$");

            this.selection = this.selection.replace(regex,
                function (match) {
                    chunkObj.endTag = match + chunkObj.endTag;
                    return "";
                });

            regex = util.extendRegExp(endRegex, "^", "");

            this.after = this.after.replace(regex,
                function (match) {
                    chunkObj.endTag = match + chunkObj.endTag;
                    return "";
                });
        }
    };

    // If remove is false, the whitespace is transferred
    // to the before/after regions.
    //
    // If remove is true, the whitespace disappears.
    Chunks.prototype.trimWhitespace = function (remove) {
        var beforeReplacer, afterReplacer, that = this;
        if (remove) {
            beforeReplacer = afterReplacer = "";
        } else {
            beforeReplacer = function (s) { that.before += s; return ""; }
            afterReplacer = function (s) { that.after = s + that.after; return ""; }
        }

        this.selection = this.selection.replace(/^(\s*)/, beforeReplacer).replace(/(\s*)$/, afterReplacer);
    };


    Chunks.prototype.skipLines = function (nLinesBefore, nLinesAfter, findExtraNewlines) {

        if (nLinesBefore === undefined) {
            nLinesBefore = 1;
        }

        if (nLinesAfter === undefined) {
            nLinesAfter = 1;
        }

        nLinesBefore++;
        nLinesAfter++;

        var regexText;
        var replacementText;

        // chrome bug ... documented at: http://meta.stackoverflow.com/questions/63307/blockquote-glitch-in-editor-in-chrome-6-and-7/65985#65985
        if (navigator.userAgent.match(/Chrome/)) {
            "X".match(/()./);
        }

        this.selection = this.selection.replace(/(^\n*)/, "");

        this.startTag = this.startTag + re.$1;

        this.selection = this.selection.replace(/(\n*$)/, "");
        this.endTag = this.endTag + re.$1;
        this.startTag = this.startTag.replace(/(^\n*)/, "");
        this.before = this.before + re.$1;
        this.endTag = this.endTag.replace(/(\n*$)/, "");
        this.after = this.after + re.$1;

        if (this.before) {

            regexText = replacementText = "";

            while (nLinesBefore--) {
                regexText += "\\n?";
                replacementText += "\n";
            }

            if (findExtraNewlines) {
                regexText = "\\n*";
            }
            this.before = this.before.replace(new re(regexText + "$", ""), replacementText);
        }

        if (this.after) {

            regexText = replacementText = "";

            while (nLinesAfter--) {
                regexText += "\\n?";
                replacementText += "\n";
            }
            if (findExtraNewlines) {
                regexText = "\\n*";
            }

            this.after = this.after.replace(new re(regexText, ""), replacementText);
        }
    };

    // end of Chunks

    // A collection of the important regions on the page.
    // Cached so we don't have to keep traversing the DOM.
    // Also holds ieCachedRange and ieCachedScrollTop, where necessary; working around
    // this issue:
    // Internet explorer has problems with CSS sprite buttons that use HTML
    // lists.  When you click on the background image "button", IE will
    // select the non-existent link text and discard the selection in the
    // textarea.  The solution to this is to cache the textarea selection
    // on the button's mousedown event and set a flag.  In the part of the
    // code where we need to grab the selection, we check for the flag
    // and, if it's set, use the cached area instead of querying the
    // textarea.
    //
    // This ONLY affects Internet Explorer (tested on versions 6, 7
    // and 8) and ONLY on button clicks.  Keyboard shortcuts work
    // normally since the focus never leaves the textarea.
    function PanelCollection(postfix) {
        this.buttonBar = doc.getElementById("wmd-button-bar" + postfix);
        this.preview = doc.getElementById("wmd-preview" + postfix);
        this.input = doc.getElementById("wmd-input" + postfix);
    };

    // Returns true if the DOM element is visible, false if it's hidden.
    // Checks if display is anything other than none.
    util.isVisible = function (elem) {

        if (window.getComputedStyle) {
            // Most browsers
            return window.getComputedStyle(elem, null).getPropertyValue("display") !== "none";
        }
        else if (elem.currentStyle) {
            // IE
            return elem.currentStyle["display"] !== "none";
        }
    };


    // Adds a listener callback to a DOM element which is fired on a specified
    // event.
    util.addEvent = function (elem, event, listener) {
        if (elem.attachEvent) {
            // IE only.  The "on" is mandatory.
            elem.attachEvent("on" + event, listener);
        }
        else {
            // Other browsers.
            elem.addEventListener(event, listener, false);
        }
    };


    // Removes a listener callback from a DOM element which is fired on a specified
    // event.
    util.removeEvent = function (elem, event, listener) {
        if (elem.detachEvent) {
            // IE only.  The "on" is mandatory.
            elem.detachEvent("on" + event, listener);
        }
        else {
            // Other browsers.
            elem.removeEventListener(event, listener, false);
        }
    };

    // Converts \r\n and \r to \n.
    util.fixEolChars = function (text) {
        text = text.replace(/\r\n/g, "\n");
        text = text.replace(/\r/g, "\n");
        return text;
    };

    // Extends a regular expression.  Returns a new RegExp
    // using pre + regex + post as the expression.
    // Used in a few functions where we have a base
    // expression and we want to pre- or append some
    // conditions to it (e.g. adding "$" to the end).
    // The flags are unchanged.
    //
    // regex is a RegExp, pre and post are strings.
    util.extendRegExp = function (regex, pre, post) {

        if (pre === null || pre === undefined) {
            pre = "";
        }
        if (post === null || post === undefined) {
            post = "";
        }

        var pattern = regex.toString();
        var flags;

        // Replace the flags with empty space and store them.
        pattern = pattern.replace(/\/([gim]*)$/, function (wholeMatch, flagsPart) {
            flags = flagsPart;
            return "";
        });

        // Remove the slash delimiters on the regular expression.
        pattern = pattern.replace(/(^\/|\/$)/g, "");
        pattern = pre + pattern + post;

        return new re(pattern, flags);
    }

    // UNFINISHED
    // The assignment in the while loop makes jslint cranky.
    // I'll change it to a better loop later.
    position.getTop = function (elem, isInner) {
        var result = elem.offsetTop;
        if (!isInner) {
            while (elem = elem.offsetParent) {
                result += elem.offsetTop;
            }
        }
        return result;
    };

    position.getHeight = function (elem) {
        return elem.offsetHeight || elem.scrollHeight;
    };

    position.getWidth = function (elem) {
        return elem.offsetWidth || elem.scrollWidth;
    };

    position.getPageSize = function () {

        var scrollWidth, scrollHeight;
        var innerWidth, innerHeight;

        // It's not very clear which blocks work with which browsers.
        if (self.innerHeight && self.scrollMaxY) {
            scrollWidth = doc.body.scrollWidth;
            scrollHeight = self.innerHeight + self.scrollMaxY;
        }
        else if (doc.body.scrollHeight > doc.body.offsetHeight) {
            scrollWidth = doc.body.scrollWidth;
            scrollHeight = doc.body.scrollHeight;
        }
        else {
            scrollWidth = doc.body.offsetWidth;
            scrollHeight = doc.body.offsetHeight;
        }

        if (self.innerHeight) {
            // Non-IE browser
            innerWidth = self.innerWidth;
            innerHeight = self.innerHeight;
        }
        else if (doc.documentElement && doc.documentElement.clientHeight) {
            // Some versions of IE (IE 6 w/ a DOCTYPE declaration)
            innerWidth = doc.documentElement.clientWidth;
            innerHeight = doc.documentElement.clientHeight;
        }
        else if (doc.body) {
            // Other versions of IE
            innerWidth = doc.body.clientWidth;
            innerHeight = doc.body.clientHeight;
        }

        var maxWidth = Math.max(scrollWidth, innerWidth);
        var maxHeight = Math.max(scrollHeight, innerHeight);
        return [maxWidth, maxHeight, innerWidth, innerHeight];
    };

    // Handles pushing and popping TextareaStates for undo/redo commands.
    // I should rename the stack variables to list.
    function UndoManager(callback, panels) {

        var undoObj = this;
        var undoStack = []; // A stack of undo states
        var stackPtr = 0; // The index of the current state
        var mode = "none";
        var lastState; // The last state
        var timer; // The setTimeout handle for cancelling the timer
        var inputStateObj;

        // Set the mode for later logic steps.
        var setMode = function (newMode, noSave) {
            if (mode != newMode) {
                mode = newMode;
                if (!noSave) {
                    saveState();
                }
            }

            if (!uaSniffed.isIE || mode != "moving") {
                timer = setTimeout(refreshState, 1);
            }
            else {
                inputStateObj = null;
            }
        };

        var refreshState = function (isInitialState) {
            inputStateObj = new TextareaState(panels, isInitialState);
            timer = undefined;
        };

        this.setCommandMode = function () {
            mode = "command";
            saveState();
            timer = setTimeout(refreshState, 0);
        };

        this.canUndo = function () {
            return stackPtr > 1;
        };

        this.canRedo = function () {
            if (undoStack[stackPtr + 1]) {
                return true;
            }
            return false;
        };

        // Removes the last state and restores it.
        this.undo = function () {

            if (undoObj.canUndo()) {
                if (lastState) {
                    // What about setting state -1 to null or checking for undefined?
                    lastState.restore();
                    lastState = null;
                }
                else {
                    undoStack[stackPtr] = new TextareaState(panels);
                    undoStack[--stackPtr].restore();

                    if (callback) {
                        callback();
                    }
                }
            }

            mode = "none";
            panels.input.focus();
            refreshState();
        };

        // Redo an action.
        this.redo = function () {

            if (undoObj.canRedo()) {

                undoStack[++stackPtr].restore();

                if (callback) {
                    callback();
                }
            }

            mode = "none";
            panels.input.focus();
            refreshState();
        };

        // Push the input area state to the stack.
        var saveState = function () {
            var currState = inputStateObj || new TextareaState(panels);

            if (!currState) {
                return false;
            }
            if (mode == "moving") {
                if (!lastState) {
                    lastState = currState;
                }
                return;
            }
            if (lastState) {
                if (undoStack[stackPtr - 1].text != lastState.text) {
                    undoStack[stackPtr++] = lastState;
                }
                lastState = null;
            }
            undoStack[stackPtr++] = currState;
            undoStack[stackPtr + 1] = null;
            if (callback) {
                callback();
            }
        };

        var handleCtrlYZ = function (event) {

            var handled = false;

            if ((event.ctrlKey || event.metaKey) && !event.altKey) {

                // IE and Opera do not support charCode.
                var keyCode = event.charCode || event.keyCode;
                var keyCodeChar = String.fromCharCode(keyCode);

                switch (keyCodeChar.toLowerCase()) {

                    case "y":
                        undoObj.redo();
                        handled = true;
                        break;

                    case "z":
                        if (!event.shiftKey) {
                            undoObj.undo();
                        }
                        else {
                            undoObj.redo();
                        }
                        handled = true;
                        break;
                }
            }

            if (handled) {
                if (event.preventDefault) {
                    event.preventDefault();
                }
                if (window.event) {
                    window.event.returnValue = false;
                }
                return;
            }
        };

        // Set the mode depending on what is going on in the input area.
        var handleModeChange = function (event) {

            if (!event.ctrlKey && !event.metaKey) {

                var keyCode = event.keyCode;

                if ((keyCode >= 33 && keyCode <= 40) || (keyCode >= 63232 && keyCode <= 63235)) {
                    // 33 - 40: page up/dn and arrow keys
                    // 63232 - 63235: page up/dn and arrow keys on safari
                    setMode("moving");
                }
                else if (keyCode == 8 || keyCode == 46 || keyCode == 127) {
                    // 8: backspace
                    // 46: delete
                    // 127: delete
                    setMode("deleting");
                }
                else if (keyCode == 13) {
                    // 13: Enter
                    setMode("newlines");
                }
                else if (keyCode == 27) {
                    // 27: escape
                    setMode("escape");
                }
                else if ((keyCode < 16 || keyCode > 20) && keyCode != 91) {
                    // 16-20 are shift, etc.
                    // 91: left window key
                    // I think this might be a little messed up since there are
                    // a lot of nonprinting keys above 20.
                    setMode("typing");
                }
            }
        };

        var setEventHandlers = function () {
            util.addEvent(panels.input, "keypress", function (event) {
                // keyCode 89: y
                // keyCode 90: z
                if ((event.ctrlKey || event.metaKey) && !event.altKey && (event.keyCode == 89 || event.keyCode == 90)) {
                    event.preventDefault();
                }
            });

            var handlePaste = function () {
                if (uaSniffed.isIE || (inputStateObj && inputStateObj.text != panels.input.value)) {
                    if (timer == undefined) {
                        mode = "paste";
                        saveState();
                        refreshState();
                    }
                }
            };

            util.addEvent(panels.input, "keydown", handleCtrlYZ);
            util.addEvent(panels.input, "keydown", handleModeChange);
            util.addEvent(panels.input, "mousedown", function () {
                setMode("moving");
            });

            panels.input.onpaste = handlePaste;
            panels.input.ondrop = handlePaste;
        };

        var init = function () {
            setEventHandlers();
            refreshState(true);
            saveState();
        };

        init();
    }

    // end of UndoManager

    // The input textarea state/contents.
    // This is used to implement undo/redo by the undo manager.
    function TextareaState(panels, isInitialState) {

        // Aliases
        var stateObj = this;
        var inputArea = panels.input;
        this.init = function () {
            if (!util.isVisible(inputArea)) {
                return;
            }
            if (!isInitialState && doc.activeElement && doc.activeElement !== inputArea) { // this happens when tabbing out of the input box
                return;
            }

            this.setInputAreaSelectionStartEnd();
            this.scrollTop = inputArea.scrollTop;
            if (!this.text && inputArea.selectionStart || inputArea.selectionStart === 0) {
                this.text = inputArea.value;
            }

        }

        // Sets the selected text in the input box after we've performed an
        // operation.
        this.setInputAreaSelection = function () {

            if (!util.isVisible(inputArea)) {
                return;
            }

            if (inputArea.selectionStart !== undefined && !uaSniffed.isOpera) {

                inputArea.focus();
                inputArea.selectionStart = stateObj.start;
                inputArea.selectionEnd = stateObj.end;
                inputArea.scrollTop = stateObj.scrollTop;
            }
            else if (doc.selection) {

                if (doc.activeElement && doc.activeElement !== inputArea) {
                    return;
                }

                inputArea.focus();
                var range = inputArea.createTextRange();
                range.moveStart("character", -inputArea.value.length);
                range.moveEnd("character", -inputArea.value.length);
                range.moveEnd("character", stateObj.end);
                range.moveStart("character", stateObj.start);
                range.select();
            }
        };

        this.setInputAreaSelectionStartEnd = function () {

            if (!panels.ieCachedRange && (inputArea.selectionStart || inputArea.selectionStart === 0)) {

                stateObj.start = inputArea.selectionStart;
                stateObj.end = inputArea.selectionEnd;
            }
            else if (doc.selection) {

                stateObj.text = util.fixEolChars(inputArea.value);

                // IE loses the selection in the textarea when buttons are
                // clicked.  On IE we cache the selection. Here, if something is cached,
                // we take it.
                var range = panels.ieCachedRange || doc.selection.createRange();

                var fixedRange = util.fixEolChars(range.text);
                var marker = "\x07";
                var markedRange = marker + fixedRange + marker;
                range.text = markedRange;
                var inputText = util.fixEolChars(inputArea.value);

                range.moveStart("character", -markedRange.length);
                range.text = fixedRange;

                stateObj.start = inputText.indexOf(marker);
                stateObj.end = inputText.lastIndexOf(marker) - marker.length;

                var len = stateObj.text.length - util.fixEolChars(inputArea.value).length;

                if (len) {
                    range.moveStart("character", -fixedRange.length);
                    while (len--) {
                        fixedRange += "\n";
                        stateObj.end += 1;
                    }
                    range.text = fixedRange;
                }

                if (panels.ieCachedRange)
                    stateObj.scrollTop = panels.ieCachedScrollTop; // this is set alongside with ieCachedRange

                panels.ieCachedRange = null;

                this.setInputAreaSelection();
            }
        };

        // Restore this state into the input area.
        this.restore = function () {

            if (stateObj.text != undefined && stateObj.text != inputArea.value) {
                inputArea.value = stateObj.text;
            }
            this.setInputAreaSelection();
            inputArea.scrollTop = stateObj.scrollTop;
        };

        // Gets a collection of HTML chunks from the inptut textarea.
        this.getChunks = function () {

            var chunk = new Chunks();
            chunk.before = util.fixEolChars(stateObj.text.substring(0, stateObj.start));
            chunk.startTag = "";
            chunk.selection = util.fixEolChars(stateObj.text.substring(stateObj.start, stateObj.end));
            chunk.endTag = "";
            chunk.after = util.fixEolChars(stateObj.text.substring(stateObj.end));
            chunk.scrollTop = stateObj.scrollTop;

            return chunk;
        };

        // Sets the TextareaState properties given a chunk of markdown.
        this.setChunks = function (chunk) {

            chunk.before = chunk.before + chunk.startTag;
            chunk.after = chunk.endTag + chunk.after;

            this.start = chunk.before.length;
            this.end = chunk.before.length + chunk.selection.length;
            this.text = chunk.before + chunk.selection + chunk.after;
            this.scrollTop = chunk.scrollTop;
        };
        this.init();
    };

    function PreviewManager(converter, panels, previewRefreshCallback) {

        var managerObj = this;
        var timeout;
        var elapsedTime;
        var oldInputText;
        var maxDelay = 3000;
        var startType = "delayed"; // The other legal value is "manual"

        // Adds event listeners to elements
        var setupEvents = function (inputElem, listener) {

            util.addEvent(inputElem, "input", listener);
            inputElem.onpaste = listener;
            inputElem.ondrop = listener;

            util.addEvent(inputElem, "keypress", listener);
            util.addEvent(inputElem, "keydown", listener);
        };

        var getDocScrollTop = function () {

            var result = 0;

            if (window.innerHeight) {
                result = window.pageYOffset;
            }
            else
                if (doc.documentElement && doc.documentElement.scrollTop) {
                    result = doc.documentElement.scrollTop;
                }
                else
                    if (doc.body) {
                        result = doc.body.scrollTop;
                    }

            return result;
        };

        var makePreviewHtml = function () {

            // If there is no registered preview panel
            // there is nothing to do.
            if (!panels.preview)
                return;


            var text = panels.input.value;
            if (text && text == oldInputText) {
                return; // Input text hasn't changed.
            }
            else {
                oldInputText = text;
            }

            var prevTime = new Date().getTime();

            text = converter.makeHtml(text);

            // Calculate the processing time of the HTML creation.
            // It's used as the delay time in the event listener.
            var currTime = new Date().getTime();
            elapsedTime = currTime - prevTime;

            pushPreviewHtml(text);
        };

        // setTimeout is already used.  Used as an event listener.
        var applyTimeout = function () {

            if (timeout) {
                clearTimeout(timeout);
                timeout = undefined;
            }

            if (startType !== "manual") {

                var delay = 0;

                if (startType === "delayed") {
                    delay = elapsedTime;
                }

                if (delay > maxDelay) {
                    delay = maxDelay;
                }
                timeout = setTimeout(makePreviewHtml, delay);
            }
        };

        var getScaleFactor = function (panel) {
            if (panel.scrollHeight <= panel.clientHeight) {
                return 1;
            }
            return panel.scrollTop / (panel.scrollHeight - panel.clientHeight);
        };

        var setPanelScrollTops = function () {
            if (panels.preview) {
                panels.preview.scrollTop = (panels.preview.scrollHeight - panels.preview.clientHeight) * getScaleFactor(panels.preview);
            }
        };

        this.refresh = function (requiresRefresh) {

            if (requiresRefresh) {
                oldInputText = "";
                makePreviewHtml();
            }
            else {
                applyTimeout();
            }
        };

        this.processingTime = function () {
            return elapsedTime;
        };

        var isFirstTimeFilled = true;

        // IE doesn't let you use innerHTML if the element is contained somewhere in a table
        // (which is the case for inline editing) -- in that case, detach the element, set the
        // value, and reattach. Yes, that *is* ridiculous.
        var ieSafePreviewSet = function (text) {
            var preview = panels.preview;
            var parent = preview.parentNode;
            var sibling = preview.nextSibling;
            parent.removeChild(preview);
            preview.innerHTML = text;
            if (!sibling)
                parent.appendChild(preview);
            else
                parent.insertBefore(preview, sibling);
        }

        var nonSuckyBrowserPreviewSet = function (text) {
            panels.preview.innerHTML = text;
        }

        var previewSetter;

        var previewSet = function (text) {
            if (previewSetter)
                return previewSetter(text);

            try {
                nonSuckyBrowserPreviewSet(text);
                previewSetter = nonSuckyBrowserPreviewSet;
            } catch (e) {
                previewSetter = ieSafePreviewSet;
                previewSetter(text);
            }
        };

        var pushPreviewHtml = function (text) {

            var emptyTop = position.getTop(panels.input) - getDocScrollTop();

            if (panels.preview) {
                previewSet(text);
                previewRefreshCallback();
            }

            setPanelScrollTops();

            if (isFirstTimeFilled) {
                isFirstTimeFilled = false;
                return;
            }

            var fullTop = position.getTop(panels.input) - getDocScrollTop();

            if (uaSniffed.isIE) {
                setTimeout(function () {
                    window.scrollBy(0, fullTop - emptyTop);
                }, 0);
            }
            else {
                window.scrollBy(0, fullTop - emptyTop);
            }
        };

        var init = function () {

            setupEvents(panels.input, applyTimeout);
            makePreviewHtml();

            if (panels.preview) {
                panels.preview.scrollTop = 0;
            }
        };

        init();
    };

    // Creates the background behind the hyperlink text entry box.
    // And download dialog
    // Most of this has been moved to CSS but the div creation and
    // browser-specific hacks remain here.
    ui.createBackground = function () {

        var background = doc.createElement("div"),
            style = background.style;
        
        background.className = "wmd-prompt-background";
        
        style.position = "absolute";
        style.top = "0";

        style.zIndex = "1000";

        if (uaSniffed.isIE) {
            style.filter = "alpha(opacity=50)";
        }
        else {
            style.opacity = "0.5";
        }

        var pageSize = position.getPageSize();
        style.height = pageSize[1] + "px";

        if (uaSniffed.isIE) {
            style.left = doc.documentElement.scrollLeft;
            style.width = doc.documentElement.clientWidth;
        }
        else {
            style.left = "0";
            style.width = "100%";
        }

        doc.body.appendChild(background);
        return background;
    };

    // This simulates a modal dialog box and asks for the URL when you
    // click the hyperlink or image buttons.
    //
    // text: The html for the input box.
    // defaultInputText: The default value that appears in the input box.
    // callback: The function which is executed when the prompt is dismissed, either via OK or Cancel.
    //      It receives a single argument; either the entered text (if OK was chosen) or null (if Cancel
    //      was chosen).
    ui.prompt = function (text, defaultInputText, callback) {

        // These variables need to be declared at this level since they are used
        // in multiple functions.
        var dialog;         // The dialog box.
        var input;         // The text box where you enter the hyperlink.


        if (defaultInputText === undefined) {
            defaultInputText = "";
        }

        // Used as a keydown event handler. Esc dismisses the prompt.
        // Key code 27 is ESC.
        var checkEscape = function (key) {
            var code = (key.charCode || key.keyCode);
            if (code === 27) {
                close(true);
            }
        };

        // Dismisses the hyperlink input box.
        // isCancel is true if we don't care about the input text.
        // isCancel is false if we are going to keep the text.
        var close = function (isCancel) {
            util.removeEvent(doc.body, "keydown", checkEscape);
            var text = input.value;

            if (isCancel) {
                text = null;
            }
            else {
                // Fixes common pasting errors.
                text = text.replace(/^http:\/\/(https?|ftp):\/\//, '$1://');
                if (!/^(?:https?|ftp):\/\//.test(text))
                    text = 'http://' + text;
            }

            dialog.parentNode.removeChild(dialog);

            callback(text);
            return false;
        };



        // Create the text input box form/window.
        var createDialog = function () {

            // The main dialog box.
            dialog = doc.createElement("div");
            dialog.className = "wmd-prompt-dialog";
            dialog.style.padding = "10px;";
            dialog.style.position = "fixed";
            dialog.style.width = "400px";
            dialog.style.zIndex = "1001";

            // The dialog text.
            var question = doc.createElement("div");
            question.innerHTML = text;
            question.style.padding = "5px";
            dialog.appendChild(question);

            // The web form container for the text box and buttons.
            var form = doc.createElement("form"),
                style = form.style;
            form.onsubmit = function () { return close(false); };
            style.padding = "0";
            style.margin = "0";
            style.cssFloat = "left";
            style.width = "100%";
            style.textAlign = "center";
            style.position = "relative";
            dialog.appendChild(form);

            // The input text box
            input = doc.createElement("input");
            input.type = "text";
            input.value = defaultInputText;
            style = input.style;
            style.display = "block";
            style.width = "80%";
            style.marginLeft = style.marginRight = "auto";
            form.appendChild(input);

            // The ok button
            var okButton = doc.createElement("input");
            okButton.type = "button";
            okButton.onclick = function () { return close(false); };
            okButton.value = "OK";
            style = okButton.style;
            style.margin = "10px";
            style.display = "inline";
            style.width = "7em";


            // The cancel button
            var cancelButton = doc.createElement("input");
            cancelButton.type = "button";
            cancelButton.onclick = function () { return close(true); };
            cancelButton.value = "Cancel";
            style = cancelButton.style;
            style.margin = "10px";
            style.display = "inline";
            style.width = "7em";

            form.appendChild(okButton);
            form.appendChild(cancelButton);

            util.addEvent(doc.body, "keydown", checkEscape);
            dialog.style.top = "50%";
            dialog.style.left = "50%";
            dialog.style.display = "block";
            if (uaSniffed.isIE_5or6) {
                dialog.style.position = "absolute";
                dialog.style.top = doc.documentElement.scrollTop + 200 + "px";
                dialog.style.left = "50%";
            }
            doc.body.appendChild(dialog);

            // This has to be done AFTER adding the dialog to the form if you
            // want it to be centered.
            dialog.style.marginTop = -(position.getHeight(dialog) / 2) + "px";
            dialog.style.marginLeft = -(position.getWidth(dialog) / 2) + "px";

        };

        // Why is this in a zero-length timeout?
        // Is it working around a browser bug?
        setTimeout(function () {

            createDialog();

            var defTextLen = defaultInputText.length;
            if (input.selectionStart !== undefined) {
                input.selectionStart = 0;
                input.selectionEnd = defTextLen;
            }
            else if (input.createTextRange) {
                var range = input.createTextRange();
                range.collapse(false);
                range.moveStart("character", -defTextLen);
                range.moveEnd("character", defTextLen);
                range.select();
            }

            input.focus();
        }, 0);
    };

    function UIManager(postfix, panels, undoManager, previewManager, commandManager, helpOptions, getString) {

        var inputBox = panels.input,
            buttons = {}; // buttons.undo, buttons.link, etc. The actual DOM elements.

        makeSpritedButtonRow();

        var keyEvent = "keydown";
        if (uaSniffed.isOpera) {
            keyEvent = "keypress";
        }

        util.addEvent(inputBox, keyEvent, function (key) {

            // Check to see if we have a button key and, if so execute the callback.
            if ((key.ctrlKey || key.metaKey) && !key.altKey && !key.shiftKey) {

                var keyCode = key.charCode || key.keyCode;
                var keyCodeStr = String.fromCharCode(keyCode).toLowerCase();

                switch (keyCodeStr) {
                    case "b":
                        doClick(buttons.bold);
                        break;
                    case "i":
                        doClick(buttons.italic);
                        break;
                    case "l":
                        doClick(buttons.link);
                        break;
                    case "q":
                        doClick(buttons.quote);
                        break;
                    case "k":
                        doClick(buttons.code);
                        break;
                    case "g":
                        doClick(buttons.image);
                        break;
                    case "o":
                        doClick(buttons.olist);
                        break;
                    case "u":
                        doClick(buttons.ulist);
                        break;
                    case "h":
                        doClick(buttons.heading);
                        break;
                    case "r":
                        doClick(buttons.hr);
                        break;
                    case "y":
                        doClick(buttons.redo);
                        break;
                    case "z":
                        if (key.shiftKey) {
                            doClick(buttons.redo);
                        }
                        else {
                            doClick(buttons.undo);
                        }
                        break;
                    default:
                        return;
                }


                if (key.preventDefault) {
                    key.preventDefault();
                }

                if (window.event) {
                    window.event.returnValue = false;
                }
            }
        });

        // Auto-indent on shift-enter
        util.addEvent(inputBox, "keyup", function (key) {
            if (key.shiftKey && !key.ctrlKey && !key.metaKey) {
                var keyCode = key.charCode || key.keyCode;
                // Character 13 is Enter
                if (keyCode === 13) {
                    var fakeButton = {};
                    fakeButton.textOp = bindCommand("doAutoindent");
                    doClick(fakeButton);
                }
            }
        });

        // special handler because IE clears the context of the textbox on ESC
        if (uaSniffed.isIE) {
            util.addEvent(inputBox, "keydown", function (key) {
                var code = key.keyCode;
                if (code === 27) {
                    return false;
                }
            });
        }


        // Perform the button's action.
        function doClick(button) {

            inputBox.focus();

            if (button.textOp) {

                if (undoManager) {
                    undoManager.setCommandMode();
                }

                var state = new TextareaState(panels);

                if (!state) {
                    return;
                }

                var chunks = state.getChunks();

                // Some commands launch a "modal" prompt dialog.  Javascript
                // can't really make a modal dialog box and the WMD code
                // will continue to execute while the dialog is displayed.
                // This prevents the dialog pattern I'm used to and means
                // I can't do something like this:
                //
                // var link = CreateLinkDialog();
                // makeMarkdownLink(link);
                //
                // Instead of this straightforward method of handling a
                // dialog I have to pass any code which would execute
                // after the dialog is dismissed (e.g. link creation)
                // in a function parameter.
                //
                // Yes this is awkward and I think it sucks, but there's
                // no real workaround.  Only the image and link code
                // create dialogs and require the function pointers.
                var fixupInputArea = function () {

                    inputBox.focus();

                    if (chunks) {
                        state.setChunks(chunks);
                    }

                    state.restore();
                    previewManager.refresh();
                };

                var noCleanup = button.textOp(chunks, fixupInputArea);

                if (!noCleanup) {
                    fixupInputArea();
                }

            }

            if (button.execute) {
                button.execute(undoManager);
            }
        };

        function setupButton(button, isEnabled) {

            var normalYShift = "0px";
            var disabledYShift = "-20px";
            var highlightYShift = "-40px";
            var image = button.getElementsByTagName("span")[0];
            if (isEnabled) {
                image.style.backgroundPosition = button.XShift + " " + normalYShift;
                button.onmouseover = function () {
                    image.style.backgroundPosition = this.XShift + " " + highlightYShift;
                };

                button.onmouseout = function () {
                    image.style.backgroundPosition = this.XShift + " " + normalYShift;
                };

                // IE tries to select the background image "button" text (it's
                // implemented in a list item) so we have to cache the selection
                // on mousedown.
                if (uaSniffed.isIE) {
                    button.onmousedown = function () {
                        if (doc.activeElement && doc.activeElement !== panels.input) { // we're not even in the input box, so there's no selection
                            return;
                        }
                        panels.ieCachedRange = document.selection.createRange();
                        panels.ieCachedScrollTop = panels.input.scrollTop;
                    };
                }

                if (!button.isHelp) {
                    button.onclick = function () {
                        if (this.onmouseout) {
                            this.onmouseout();
                        }
                        doClick(this);
                        return false;
                    }
                }
            }
            else {
                image.style.backgroundPosition = button.XShift + " " + disabledYShift;
                button.onmouseover = button.onmouseout = button.onclick = function () { };
            }
        }

        function bindCommand(method) {
            if (typeof method === "string")
                method = commandManager[method];
            return function () { method.apply(commandManager, arguments); }
        }

        function makeSpritedButtonRow() {

            var buttonBar = panels.buttonBar;

            var normalYShift = "0px";
            var disabledYShift = "-20px";
            var highlightYShift = "-40px";

            var buttonRow = document.createElement("ul");
            buttonRow.id = "wmd-button-row" + postfix;
            buttonRow.className = 'wmd-button-row';
            buttonRow = buttonBar.appendChild(buttonRow);
            var xPosition = 0;
            var makeButton = function (id, title, XShift, textOp) {
                var button = document.createElement("li");
                button.className = "wmd-button";
                button.style.left = xPosition + "px";
                xPosition += 25;
                var buttonImage = document.createElement("span");
                button.id = id + postfix;
                button.appendChild(buttonImage);
                button.title = title;
                button.XShift = XShift;
                if (textOp)
                    button.textOp = textOp;
                setupButton(button, true);
                buttonRow.appendChild(button);
                return button;
            };
            var makeSpacer = function (num) {
                var spacer = document.createElement("li");
                spacer.className = "wmd-spacer wmd-spacer" + num;
                spacer.id = "wmd-spacer" + num + postfix;
                buttonRow.appendChild(spacer);
                xPosition += 25;
            }

            buttons.bold = makeButton("wmd-bold-button", getString("bold"), "0px", bindCommand("doBold"));
            buttons.italic = makeButton("wmd-italic-button", getString("italic"), "-20px", bindCommand("doItalic"));
            makeSpacer(1);
            buttons.link = makeButton("wmd-link-button", getString("link"), "-40px", bindCommand(function (chunk, postProcessing) {
                return this.doLinkOrImage(chunk, postProcessing, false);
            }));
            buttons.quote = makeButton("wmd-quote-button", getString("quote"), "-60px", bindCommand("doBlockquote"));
            buttons.code = makeButton("wmd-code-button", getString("code"), "-80px", bindCommand("doCode"));
            buttons.image = makeButton("wmd-image-button", getString("image"), "-100px", bindCommand(function (chunk, postProcessing) {
                return this.doImage(chunk, postProcessing);
            }));
            makeSpacer(2);
            buttons.olist = makeButton("wmd-olist-button", getString("olist"), "-120px", bindCommand(function (chunk, postProcessing) {
                this.doList(chunk, postProcessing, true);
            }));
            buttons.ulist = makeButton("wmd-ulist-button", getString("ulist"), "-140px", bindCommand(function (chunk, postProcessing) {
                this.doList(chunk, postProcessing, false);
            }));
            buttons.heading = makeButton("wmd-heading-button", getString("heading"), "-160px", bindCommand("doHeading"));
            buttons.hr = makeButton("wmd-hr-button", getString("hr"), "-180px", bindCommand("doHorizontalRule"));
            makeSpacer(3);
            buttons.undo = makeButton("wmd-undo-button", getString("undo"), "-200px", null);
            buttons.undo.execute = function (manager) { if (manager) manager.undo(); };

            var redoTitle = /win/.test(nav.platform.toLowerCase()) ?
                getString("redo") :
                getString("redomac"); // mac and other non-Windows platforms

            buttons.redo = makeButton("wmd-redo-button", redoTitle, "-220px", null);
            buttons.redo.execute = function (manager) { if (manager) manager.redo(); };

            if (helpOptions) {
                var helpButton = document.createElement("li");
                var helpButtonImage = document.createElement("span");
                helpButton.appendChild(helpButtonImage);
                helpButton.className = "wmd-button wmd-help-button";
                helpButton.id = "wmd-help-button" + postfix;
                helpButton.XShift = "-240px";
                helpButton.isHelp = true;
                helpButton.style.right = "0px";
                helpButton.title = getString("help");
                helpButton.onclick = helpOptions.handler;

                setupButton(helpButton, true);
                buttonRow.appendChild(helpButton);
                buttons.help = helpButton;
            }

            setUndoRedoButtonStates();
        }

        function setUndoRedoButtonStates() {
            if (undoManager) {
                setupButton(buttons.undo, undoManager.canUndo());
                setupButton(buttons.redo, undoManager.canRedo());
            }
        };

        this.setUndoRedoButtonStates = setUndoRedoButtonStates;

    }

    function CommandManager(pluginHooks, getString) {
        this.hooks = pluginHooks;
        this.getString = getString;
    }

    var commandProto = CommandManager.prototype;

    // The markdown symbols - 4 spaces = code, > = blockquote, etc.
    commandProto.prefixes = "(?:\\s{4,}|\\s*>|\\s*-\\s+|\\s*\\d+\\.|=|\\+|-|_|\\*|#|\\s*\\[[^\n]]+\\]:)";

    // Remove markdown symbols from the chunk selection.
    commandProto.unwrap = function (chunk) {
        var txt = new re("([^\\n])\\n(?!(\\n|" + this.prefixes + "))", "g");
        chunk.selection = chunk.selection.replace(txt, "$1 $2");
    };

    commandProto.wrap = function (chunk, len) {
        this.unwrap(chunk);
        var regex = new re("(.{1," + len + "})( +|$\\n?)", "gm"),
            that = this;

        chunk.selection = chunk.selection.replace(regex, function (line, marked) {
            if (new re("^" + that.prefixes, "").test(line)) {
                return line;
            }
            return marked + "\n";
        });

        chunk.selection = chunk.selection.replace(/\s+$/, "");
    };

    commandProto.doBold = function (chunk, postProcessing) {
        return this.doBorI(chunk, postProcessing, 2, this.getString("boldexample"));
    };

    commandProto.doItalic = function (chunk, postProcessing) {
        return this.doBorI(chunk, postProcessing, 1, this.getString("italicexample"));
    };

    // chunk: The selected region that will be enclosed with */**
    // nStars: 1 for italics, 2 for bold
    // insertText: If you just click the button without highlighting text, this gets inserted
    commandProto.doBorI = function (chunk, postProcessing, nStars, insertText) {

        // Get rid of whitespace and fixup newlines.
        chunk.trimWhitespace();
        chunk.selection = chunk.selection.replace(/\n{2,}/g, "\n");

        // Look for stars before and after.  Is the chunk already marked up?
        // note that these regex matches cannot fail
        var starsBefore = /(\**$)/.exec(chunk.before)[0];
        var starsAfter = /(^\**)/.exec(chunk.after)[0];

        var prevStars = Math.min(starsBefore.length, starsAfter.length);

        // Remove stars if we have to since the button acts as a toggle.
        if ((prevStars >= nStars) && (prevStars != 2 || nStars != 1)) {
            chunk.before = chunk.before.replace(re("[*]{" + nStars + "}$", ""), "");
            chunk.after = chunk.after.replace(re("^[*]{" + nStars + "}", ""), "");
        }
        else if (!chunk.selection && starsAfter) {
            // It's not really clear why this code is necessary.  It just moves
            // some arbitrary stuff around.
            chunk.after = chunk.after.replace(/^([*_]*)/, "");
            chunk.before = chunk.before.replace(/(\s?)$/, "");
            var whitespace = re.$1;
            chunk.before = chunk.before + starsAfter + whitespace;
        }
        else {

            // In most cases, if you don't have any selected text and click the button
            // you'll get a selected, marked up region with the default text inserted.
            if (!chunk.selection && !starsAfter) {
                chunk.selection = insertText;
            }

            // Add the true markup.
            var markup = nStars <= 1 ? "*" : "**"; // shouldn't the test be = ?
            chunk.before = chunk.before + markup;
            chunk.after = markup + chunk.after;
        }

        return;
    };

    commandProto.stripLinkDefs = function (text, defsToAdd) {

        text = text.replace(/^[ ]{0,3}\[(\d+)\]:[ \t]*\n?[ \t]*<?(\S+?)>?[ \t]*\n?[ \t]*(?:(\n*)["(](.+?)[")][ \t]*)?(?:\n+|$)/gm,
            function (totalMatch, id, link, newlines, title) {
                defsToAdd[id] = totalMatch.replace(/\s*$/, "");
                if (newlines) {
                    // Strip the title and return that separately.
                    defsToAdd[id] = totalMatch.replace(/["(](.+?)[")]$/, "");
                    return newlines + title;
                }
                return "";
            });

        return text;
    };

    commandProto.addLinkDef = function (chunk, linkDef) {

        var refNumber = 0; // The current reference number
        var defsToAdd = {}; //
        // Start with a clean slate by removing all previous link definitions.
        chunk.before = this.stripLinkDefs(chunk.before, defsToAdd);
        chunk.selection = this.stripLinkDefs(chunk.selection, defsToAdd);
        chunk.after = this.stripLinkDefs(chunk.after, defsToAdd);

        var defs = "";
        var regex = /(\[)((?:\[[^\]]*\]|[^\[\]])*)(\][ ]?(?:\n[ ]*)?\[)(\d+)(\])/g;

        var addDefNumber = function (def) {
            refNumber++;
            def = def.replace(/^[ ]{0,3}\[(\d+)\]:/, "  [" + refNumber + "]:");
            defs += "\n" + def;
        };

        // note that
        // a) the recursive call to getLink cannot go infinite, because by definition
        //    of regex, inner is always a proper substring of wholeMatch, and
        // b) more than one level of nesting is neither supported by the regex
        //    nor making a lot of sense (the only use case for nesting is a linked image)
        var getLink = function (wholeMatch, before, inner, afterInner, id, end) {
            inner = inner.replace(regex, getLink);
            if (defsToAdd[id]) {
                addDefNumber(defsToAdd[id]);
                return before + inner + afterInner + refNumber + end;
            }
            return wholeMatch;
        };

        chunk.before = chunk.before.replace(regex, getLink);

        if (linkDef) {
            addDefNumber(linkDef);
        }
        else {
            chunk.selection = chunk.selection.replace(regex, getLink);
        }

        var refOut = refNumber;

        chunk.after = chunk.after.replace(regex, getLink);

        if (chunk.after) {
            chunk.after = chunk.after.replace(/\n*$/, "");
        }
        if (!chunk.after) {
            chunk.selection = chunk.selection.replace(/\n*$/, "");
        }

        chunk.after += "\n\n" + defs;

        return refOut;
    };

    // takes the line as entered into the add link/as image dialog and makes
    // sure the URL and the optinal title are "nice".
    function properlyEncoded(linkdef) {
        return linkdef.replace(/^\s*(.*?)(?:\s+"(.+)")?\s*$/, function (wholematch, link, title) {
            link = link.replace(/\?.*$/, function (querypart) {
                return querypart.replace(/\+/g, " "); // in the query string, a plus and a space are identical
            });
            link = decodeURIComponent(link); // unencode first, to prevent double encoding
            link = encodeURI(link).replace(/'/g, '%27').replace(/\(/g, '%28').replace(/\)/g, '%29');
            link = link.replace(/\?.*$/, function (querypart) {
                return querypart.replace(/\+/g, "%2b"); // since we replaced plus with spaces in the query part, all pluses that now appear where originally encoded
            });
            if (title) {
                title = title.trim ? title.trim() : title.replace(/^\s*/, "").replace(/\s*$/, "");
                title = title.replace(/"/g, "quot;").replace(/\(/g, "&#40;").replace(/\)/g, "&#41;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
            }
            return title ? link + ' "' + title + '"' : link;
        });
    }
    
    commandProto.doLinkOrImage = function (chunk, postProcessing, isImage, link) {
        chunk.trimWhitespace();
        chunk.findTags(/\s*!?\[/, /\][ ]?(?:\n[ ]*)?(\[.*?\])?/);
        var background;

        if (chunk.endTag.length > 1 && chunk.startTag.length > 0) {

            chunk.startTag = chunk.startTag.replace(/!?\[/, "");
            chunk.endTag = "";
            this.addLinkDef(chunk, null);

        }
        else {
            
            // We're moving start and end tag back into the selection, since (as we're in the else block) we're not
            // *removing* a link, but *adding* one, so whatever findTags() found is now back to being part of the
            // link text. linkEnteredCallback takes care of escaping any brackets.
            chunk.selection = chunk.startTag + chunk.selection + chunk.endTag;
            chunk.startTag = chunk.endTag = "";

            if (/\n\n/.test(chunk.selection)) {
                this.addLinkDef(chunk, null);
                return;
            }
            var that = this;
            // The function to be executed when you enter a link and press OK or Cancel.
            // Marks up the link and adds the ref.
            var linkEnteredCallback = function (link) {

                background.parentNode.removeChild(background);

                if (link !== null) {
                    // (                          $1
                    //     [^\\]                  anything that's not a backslash
                    //     (?:\\\\)*              an even number (this includes zero) of backslashes
                    // )
                    // (?=                        followed by
                    //     [[\]]                  an opening or closing bracket
                    // )
                    //
                    // In other words, a non-escaped bracket. These have to be escaped now to make sure they
                    // don't count as the end of the link or similar.
                    // Note that the actual bracket has to be a lookahead, because (in case of to subsequent brackets),
                    // the bracket in one match may be the "not a backslash" character in the next match, so it
                    // should not be consumed by the first match.
                    // The "prepend a space and finally remove it" steps makes sure there is a "not a backslash" at the
                    // start of the string, so this also works if the selection begins with a bracket. We cannot solve
                    // this by anchoring with ^, because in the case that the selection starts with two brackets, this
                    // would mean a zero-width match at the start. Since zero-width matches advance the string position,
                    // the first bracket could then not act as the "not a backslash" for the second.
                    chunk.selection = (" " + chunk.selection).replace(/([^\\](?:\\\\)*)(?=[[\]])/g, "$1\\").substr(1);
                    
                    var linkDef = " [999]: " + properlyEncoded(link);

                    var num = that.addLinkDef(chunk, linkDef);
                    chunk.startTag = isImage ? "![" : "[";
                    chunk.endTag = "][" + num + "]";

                    if (!chunk.selection) {
                        if (isImage) {
                            chunk.selection = link;
                        }
                        else {
                            chunk.selection = that.getString("linkdescription");
                        }
                    }
                }
                postProcessing();
            };

            background = ui.createBackground();

            if (isImage) {
            	linkEnteredCallback(link);
            }
            else {
                ui.prompt(this.getString("linkdialog"), linkDefaultText, linkEnteredCallback);
            }
            return true;
        }
    };
    
    commandProto.doImage = function (chunk, postProcessing){
    	filepicker.setKey(INK_API_KEY);
    	var fp;
		var featherEditor = new Aviary.Feather({
			apiKey: AVIARY_API_KEY,
			apiVersion: 2,
			tools: 'crop,resize,draw,text',
			fileFormat: 'jpg',
			onClose: function(isDirty){
				if(isDirty){
					filepicker.remove(fp);
				}
			},
			onSave: function(imageID, newURL) {
				filepicker.storeUrl(
						newURL,
						function(FPFile){
							filepicker.remove(
								fp,
								function(){
									commandProto.doLinkOrImage(chunk, postProcessing, true, FPFile.url);
								}
							);
						}
				);
			
				featherEditor.close();
			},
			
			language: 'pt_BR'
		});
		
    	var preview = document.getElementById('image-editor-preview');
    	
      	filepicker.pick({
			 mimetype: 'image/*',
			 container: 'modal',
			 maxSize: 400*1024,
			 services: ['COMPUTER', 'URL']
			 },
			 
			 function(fpfile){
				 fp = fpfile;
				 preview.src = fpfile.url;
				 featherEditor.launch({
					 image: preview,
					 url: fpfile.url
				 });
			 });
    };

    // When making a list, hitting shift-enter will put your cursor on the next line
    // at the current indent level.
    commandProto.doAutoindent = function (chunk, postProcessing) {

        var commandMgr = this,
            fakeSelection = false;

        chunk.before = chunk.before.replace(/(\n|^)[ ]{0,3}([*+-]|\d+[.])[ \t]*\n$/, "\n\n");
        chunk.before = chunk.before.replace(/(\n|^)[ ]{0,3}>[ \t]*\n$/, "\n\n");
        chunk.before = chunk.before.replace(/(\n|^)[ \t]+\n$/, "\n\n");
        
        // There's no selection, end the cursor wasn't at the end of the line:
        // The user wants to split the current list item / code line / blockquote line
        // (for the latter it doesn't really matter) in two. Temporarily select the
        // (rest of the) line to achieve this.
        if (!chunk.selection && !/^[ \t]*(?:\n|$)/.test(chunk.after)) {
            chunk.after = chunk.after.replace(/^[^\n]*/, function (wholeMatch) {
                chunk.selection = wholeMatch;
                return "";
            });
            fakeSelection = true;
        }

        if (/(\n|^)[ ]{0,3}([*+-]|\d+[.])[ \t]+.*\n$/.test(chunk.before)) {
            if (commandMgr.doList) {
                commandMgr.doList(chunk);
            }
        }
        if (/(\n|^)[ ]{0,3}>[ \t]+.*\n$/.test(chunk.before)) {
            if (commandMgr.doBlockquote) {
                commandMgr.doBlockquote(chunk);
            }
        }
        if (/(\n|^)(\t|[ ]{4,}).*\n$/.test(chunk.before)) {
            if (commandMgr.doCode) {
                commandMgr.doCode(chunk);
            }
        }
        
        if (fakeSelection) {
            chunk.after = chunk.selection + chunk.after;
            chunk.selection = "";
        }
    };

    commandProto.doBlockquote = function (chunk, postProcessing) {

        chunk.selection = chunk.selection.replace(/^(\n*)([^\r]+?)(\n*)$/,
            function (totalMatch, newlinesBefore, text, newlinesAfter) {
                chunk.before += newlinesBefore;
                chunk.after = newlinesAfter + chunk.after;
                return text;
            });

        chunk.before = chunk.before.replace(/(>[ \t]*)$/,
            function (totalMatch, blankLine) {
                chunk.selection = blankLine + chunk.selection;
                return "";
            });

        chunk.selection = chunk.selection.replace(/^(\s|>)+$/, "");
        chunk.selection = chunk.selection || this.getString("quoteexample");

        // The original code uses a regular expression to find out how much of the
        // text *directly before* the selection already was a blockquote:

        /*
        if (chunk.before) {
        chunk.before = chunk.before.replace(/\n?$/, "\n");
        }
        chunk.before = chunk.before.replace(/(((\n|^)(\n[ \t]*)*>(.+\n)*.*)+(\n[ \t]*)*$)/,
        function (totalMatch) {
        chunk.startTag = totalMatch;
        return "";
        });
        */

        // This comes down to:
        // Go backwards as many lines a possible, such that each line
        //  a) starts with ">", or
        //  b) is almost empty, except for whitespace, or
        //  c) is preceeded by an unbroken chain of non-empty lines
        //     leading up to a line that starts with ">" and at least one more character
        // and in addition
        //  d) at least one line fulfills a)
        //
        // Since this is essentially a backwards-moving regex, it's susceptible to
        // catstrophic backtracking and can cause the browser to hang;
        // see e.g. http://meta.stackoverflow.com/questions/9807.
        //
        // Hence we replaced this by a simple state machine that just goes through the
        // lines and checks for a), b), and c).

        var match = "",
            leftOver = "",
            line;
        if (chunk.before) {
            var lines = chunk.before.replace(/\n$/, "").split("\n");
            var inChain = false;
            for (var i = 0; i < lines.length; i++) {
                var good = false;
                line = lines[i];
                inChain = inChain && line.length > 0; // c) any non-empty line continues the chain
                if (/^>/.test(line)) {                // a)
                    good = true;
                    if (!inChain && line.length > 1)  // c) any line that starts with ">" and has at least one more character starts the chain
                        inChain = true;
                } else if (/^[ \t]*$/.test(line)) {   // b)
                    good = true;
                } else {
                    good = inChain;                   // c) the line is not empty and does not start with ">", so it matches if and only if we're in the chain
                }
                if (good) {
                    match += line + "\n";
                } else {
                    leftOver += match + line;
                    match = "\n";
                }
            }
            if (!/(^|\n)>/.test(match)) {             // d)
                leftOver += match;
                match = "";
            }
        }

        chunk.startTag = match;
        chunk.before = leftOver;

        // end of change

        if (chunk.after) {
            chunk.after = chunk.after.replace(/^\n?/, "\n");
        }

        chunk.after = chunk.after.replace(/^(((\n|^)(\n[ \t]*)*>(.+\n)*.*)+(\n[ \t]*)*)/,
            function (totalMatch) {
                chunk.endTag = totalMatch;
                return "";
            }
        );

        var replaceBlanksInTags = function (useBracket) {

            var replacement = useBracket ? "> " : "";

            if (chunk.startTag) {
                chunk.startTag = chunk.startTag.replace(/\n((>|\s)*)\n$/,
                    function (totalMatch, markdown) {
                        return "\n" + markdown.replace(/^[ ]{0,3}>?[ \t]*$/gm, replacement) + "\n";
                    });
            }
            if (chunk.endTag) {
                chunk.endTag = chunk.endTag.replace(/^\n((>|\s)*)\n/,
                    function (totalMatch, markdown) {
                        return "\n" + markdown.replace(/^[ ]{0,3}>?[ \t]*$/gm, replacement) + "\n";
                    });
            }
        };

        if (/^(?![ ]{0,3}>)/m.test(chunk.selection)) {
            this.wrap(chunk, SETTINGS.lineLength - 2);
            chunk.selection = chunk.selection.replace(/^/gm, "> ");
            replaceBlanksInTags(true);
            chunk.skipLines();
        } else {
            chunk.selection = chunk.selection.replace(/^[ ]{0,3}> ?/gm, "");
            this.unwrap(chunk);
            replaceBlanksInTags(false);

            if (!/^(\n|^)[ ]{0,3}>/.test(chunk.selection) && chunk.startTag) {
                chunk.startTag = chunk.startTag.replace(/\n{0,2}$/, "\n\n");
            }

            if (!/(\n|^)[ ]{0,3}>.*$/.test(chunk.selection) && chunk.endTag) {
                chunk.endTag = chunk.endTag.replace(/^\n{0,2}/, "\n\n");
            }
        }

        chunk.selection = this.hooks.postBlockquoteCreation(chunk.selection);

        if (!/\n/.test(chunk.selection)) {
            chunk.selection = chunk.selection.replace(/^(> *)/,
            function (wholeMatch, blanks) {
                chunk.startTag += blanks;
                return "";
            });
        }
    };

    commandProto.doCode = function (chunk, postProcessing) {

        var hasTextBefore = /\S[ ]*$/.test(chunk.before);
        var hasTextAfter = /^[ ]*\S/.test(chunk.after);

        // Use 'four space' markdown if the selection is on its own
        // line or is multiline.
        if ((!hasTextAfter && !hasTextBefore) || /\n/.test(chunk.selection)) {

            chunk.before = chunk.before.replace(/[ ]{4}$/,
                function (totalMatch) {
                    chunk.selection = totalMatch + chunk.selection;
                    return "";
                });

            var nLinesBack = 1;
            var nLinesForward = 1;

            if (/(\n|^)(\t|[ ]{4,}).*\n$/.test(chunk.before)) {
                nLinesBack = 0;
            }
            if (/^\n(\t|[ ]{4,})/.test(chunk.after)) {
                nLinesForward = 0;
            }

            chunk.skipLines(nLinesBack, nLinesForward);
            // customization for github markdown!
            if (!chunk.selection) {
                chunk.startTag = "```\n";  
                chunk.selection = this.getString("codeexample");
                chunk.endTag = "\n```";
            }
            else {
                if (/^[ ]{0,3}\S/m.test(chunk.selection)) {
                    chunk.selection = "```\n" + chunk.selection + "\n```";
                }
                else {
                    chunk.selection = chunk.selection.replace(/^(?:[ ]{4}|[ ]{0,3}\t)/gm, "");
                }
            }
        }
        else {
            // Use backticks (`) to delimit the code block.

            chunk.trimWhitespace();
            chunk.findTags(/`/, /`/);

            if (!chunk.startTag && !chunk.endTag) {
                chunk.startTag = chunk.endTag = "`";
                if (!chunk.selection) {
                    chunk.selection = this.getString("codeexample");
                }
            }
            else if (chunk.endTag && !chunk.startTag) {
                chunk.before += chunk.endTag;
                chunk.endTag = "";
            }
            else {
                chunk.startTag = chunk.endTag = "";
            }
        }
    };

    commandProto.doList = function (chunk, postProcessing, isNumberedList) {

        // These are identical except at the very beginning and end.
        // Should probably use the regex extension function to make this clearer.
        var previousItemsRegex = /(\n|^)(([ ]{0,3}([*+-]|\d+[.])[ \t]+.*)(\n.+|\n{2,}([*+-].*|\d+[.])[ \t]+.*|\n{2,}[ \t]+\S.*)*)\n*$/;
        var nextItemsRegex = /^\n*(([ ]{0,3}([*+-]|\d+[.])[ \t]+.*)(\n.+|\n{2,}([*+-].*|\d+[.])[ \t]+.*|\n{2,}[ \t]+\S.*)*)\n*/;

        // The default bullet is a dash but others are possible.
        // This has nothing to do with the particular HTML bullet,
        // it's just a markdown bullet.
        var bullet = "-";

        // The number in a numbered list.
        var num = 1;

        // Get the item prefix - e.g. " 1. " for a numbered list, " - " for a bulleted list.
        var getItemPrefix = function () {
            var prefix;
            if (isNumberedList) {
                prefix = " " + num + ". ";
                num++;
            }
            else {
                prefix = " " + bullet + " ";
            }
            return prefix;
        };

        // Fixes the prefixes of the other list items.
        var getPrefixedItem = function (itemText) {

            // The numbering flag is unset when called by autoindent.
            if (isNumberedList === undefined) {
                isNumberedList = /^\s*\d/.test(itemText);
            }

            // Renumber/bullet the list element.
            itemText = itemText.replace(/^[ ]{0,3}([*+-]|\d+[.])\s/gm,
                function (_) {
                    return getItemPrefix();
                });

            return itemText;
        };

        chunk.findTags(/(\n|^)*[ ]{0,3}([*+-]|\d+[.])\s+/, null);

        if (chunk.before && !/\n$/.test(chunk.before) && !/^\n/.test(chunk.startTag)) {
            chunk.before += chunk.startTag;
            chunk.startTag = "";
        }

        if (chunk.startTag) {

            var hasDigits = /\d+[.]/.test(chunk.startTag);
            chunk.startTag = "";
            chunk.selection = chunk.selection.replace(/\n[ ]{4}/g, "\n");
            this.unwrap(chunk);
            chunk.skipLines();

            if (hasDigits) {
                // Have to renumber the bullet points if this is a numbered list.
                chunk.after = chunk.after.replace(nextItemsRegex, getPrefixedItem);
            }
            if (isNumberedList == hasDigits) {
                return;
            }
        }

        var nLinesUp = 1;

        chunk.before = chunk.before.replace(previousItemsRegex,
            function (itemText) {
                if (/^\s*([*+-])/.test(itemText)) {
                    bullet = re.$1;
                }
                nLinesUp = /[^\n]\n\n[^\n]/.test(itemText) ? 1 : 0;
                return getPrefixedItem(itemText);
            });

        if (!chunk.selection) {
            chunk.selection = this.getString("litem");
        }

        var prefix = getItemPrefix();

        var nLinesDown = 1;

        chunk.after = chunk.after.replace(nextItemsRegex,
            function (itemText) {
                nLinesDown = /[^\n]\n\n[^\n]/.test(itemText) ? 1 : 0;
                return getPrefixedItem(itemText);
            });

        chunk.trimWhitespace(true);
        chunk.skipLines(nLinesUp, nLinesDown, true);
        chunk.startTag = prefix;
        var spaces = prefix.replace(/./g, " ");
        this.wrap(chunk, SETTINGS.lineLength - spaces.length);
        chunk.selection = chunk.selection.replace(/\n/g, "\n" + spaces);

    };

    commandProto.doHeading = function (chunk, postProcessing) {

        // Remove leading/trailing whitespace and reduce internal spaces to single spaces.
        chunk.selection = chunk.selection.replace(/\s+/g, " ");
        chunk.selection = chunk.selection.replace(/(^\s+|\s+$)/g, "");

        // If we clicked the button with no selected text, we just
        // make a level 2 hash header around some default text.
        if (!chunk.selection) {
            chunk.startTag = "## ";
            chunk.selection = this.getString("headingexample");
            chunk.endTag = " ##";
            return;
        }

        var headerLevel = 0;     // The existing header level of the selected text.

        // Remove any existing hash heading markdown and save the header level.
        chunk.findTags(/#+[ ]*/, /[ ]*#+/);
        if (/#+/.test(chunk.startTag)) {
            headerLevel = re.lastMatch.length;
        }
        chunk.startTag = chunk.endTag = "";

        // Try to get the current header level by looking for - and = in the line
        // below the selection.
        chunk.findTags(null, /\s?(-+|=+)/);
        if (/=+/.test(chunk.endTag)) {
            headerLevel = 1;
        }
        if (/-+/.test(chunk.endTag)) {
            headerLevel = 2;
        }

        // Skip to the next line so we can create the header markdown.
        chunk.startTag = chunk.endTag = "";
        chunk.skipLines(1, 1);

        // We make a level 2 header if there is no current header.
        // If there is a header level, we substract one from the header level.
        // If it's already a level 1 header, it's removed.
        var headerLevelToCreate = headerLevel == 0 ? 2 : headerLevel - 1;

        if (headerLevelToCreate > 0) {

            // The button only creates level 1 and 2 underline headers.
            // Why not have it iterate over hash header levels?  Wouldn't that be easier and cleaner?
            var headerChar = headerLevelToCreate >= 2 ? "-" : "=";
            var len = chunk.selection.length;
            if (len > SETTINGS.lineLength) {
                len = SETTINGS.lineLength;
            }
            chunk.endTag = "\n";
            while (len--) {
                chunk.endTag += headerChar;
            }
        }
    };

    commandProto.doHorizontalRule = function (chunk, postProcessing) {
        chunk.startTag = "----------\n";
        chunk.selection = "";
        chunk.skipLines(2, 1, true);
    }


})();/**
 * marked - a markdown parser
 * Copyright (c) 2011-2013, Christopher Jeffrey. (MIT Licensed)
 * https://github.com/chjj/marked
 */

;(function() {

/**
 * Block-Level Grammar
 */

var block = {
  newline: /^\n+/,
  fences: noop,
  hr: /^( *[-*_]){3,} *(?:\n+|$)/,
  heading: /^ *(#{1,6}) *([^\n]+?) *#* *(?:\n+|$)/,
  nptable: noop,
  lheading: /^([^\n]+)\n *(=|-){3,} *\n*/,
  blockquote: /^( *>[^\n]+(\n[^\n]+)*\n*)+/,
  list: /^( *)(bull) [\s\S]+?(?:hr|\n{2,}(?! )(?!\1bull )\n*|\s*$)/,
  html: /^ *(?:comment|closed|closing) *(?:\n{2,}|\s*$)/,
  def: /^ *\[([^\]]+)\]: *<?([^\s>]+)>?(?: +["(]([^\n]+)[")])? *(?:\n+|$)/,
  table: noop,
  paragraph: /^((?:[^\n]+\n?(?!hr|heading|lheading|blockquote|tag|def))+)\n*/,
  text: /^[^\n]+/
};

block.bullet = /(?:[*+-]|\d+\.)/;
block.item = /^( *)(bull) [^\n]*(?:\n(?!\1bull )[^\n]*)*/;
block.item = replace(block.item, 'gm')
  (/bull/g, block.bullet)
  ();

block.list = replace(block.list)
  (/bull/g, block.bullet)
  ('hr', /\n+(?=(?: *[-*_]){3,} *(?:\n+|$))/)
  ();

block._tag = '(?!(?:'
  + 'a|em|strong|small|s|cite|q|dfn|abbr|data|time|code'
  + '|var|samp|kbd|sub|sup|i|b|u|mark|ruby|rt|rp|bdi|bdo'
  + '|span|br|wbr|ins|del|img)\\b)\\w+(?!:/|@)\\b';

block.html = replace(block.html)
  ('comment', /<!--[\s\S]*?-->/)
  ('closed', /<(tag)[\s\S]+?<\/\1>/)
  ('closing', /<tag(?:"[^"]*"|'[^']*'|[^'">])*?>/)
  (/tag/g, block._tag)
  ();

block.paragraph = replace(block.paragraph)
  ('hr', block.hr)
  ('heading', block.heading)
  ('lheading', block.lheading)
  ('blockquote', block.blockquote)
  ('tag', '<' + block._tag)
  ('def', block.def)
  ();

/**
 * Normal Block Grammar
 */

block.normal = merge({}, block);

/**
 * GFM Block Grammar
 */

block.gfm = merge({}, block.normal, {
  fences: /^ *(`{3,}|~{3,}) *(\w+)? *\n([\s\S]+?)\s*\1 *(?:\n+|$)/,
  paragraph: /^/
});

block.gfm.paragraph = replace(block.paragraph)
  ('(?!', '(?!' + block.gfm.fences.source.replace('\\1', '\\2') + '|')
  ();

/**
 * GFM + Tables Block Grammar
 */

block.tables = merge({}, block.gfm, {
  nptable: /^ *(\S.*\|.*)\n *([-:]+ *\|[-| :]*)\n((?:.*\|.*(?:\n|$))*)\n*/,
  table: /^ *\|(.+)\n *\|( *[-:]+[-| :]*)\n((?: *\|.*(?:\n|$))*)\n*/
});

/**
 * Block Lexer
 */

function Lexer(options) {
  this.tokens = [];
  this.tokens.links = {};
  this.options = options || marked.defaults;
  this.rules = block.normal;

  if (this.options.gfm) {
    if (this.options.tables) {
      this.rules = block.tables;
    } else {
      this.rules = block.gfm;
    }
  }
}

/**
 * Expose Block Rules
 */

Lexer.rules = block;

/**
 * Static Lex Method
 */

Lexer.lex = function(src, options) {
  var lexer = new Lexer(options);
  return lexer.lex(src);
};

/**
 * Preprocessing
 */

Lexer.prototype.lex = function(src) {
  src = src
    .replace(/\r\n|\r/g, '\n')
    .replace(/\t/g, '    ')
    .replace(/\u00a0/g, ' ')
    .replace(/\u2424/g, '\n');

  return this.token(src, true);
};

/**
 * Lexing
 */

Lexer.prototype.token = function(src, top) {
  var src = src.replace(/^ +$/gm, '')
    , next
    , loose
    , cap
    , bull
    , b
    , item
    , space
    , i
    , l;

  while (src) {
    // newline
    if (cap = this.rules.newline.exec(src)) {
      src = src.substring(cap[0].length);
      if (cap[0].length > 1) {
        this.tokens.push({
          type: 'space'
        });
      }
    }

    // fences (gfm)
    if (cap = this.rules.fences.exec(src)) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'code',
        lang: cap[2],
        text: cap[3]
      });
      continue;
    }

    // heading
    if (cap = this.rules.heading.exec(src)) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'heading',
        depth: cap[1].length,
        text: cap[2]
      });
      continue;
    }

    // table no leading pipe (gfm)
    if (top && (cap = this.rules.nptable.exec(src))) {
      src = src.substring(cap[0].length);

      item = {
        type: 'table',
        header: cap[1].replace(/^ *| *\| *$/g, '').split(/ *\| */),
        align: cap[2].replace(/^ *|\| *$/g, '').split(/ *\| */),
        cells: cap[3].replace(/\n$/, '').split('\n')
      };

      for (i = 0; i < item.align.length; i++) {
        if (/^ *-+: *$/.test(item.align[i])) {
          item.align[i] = 'right';
        } else if (/^ *:-+: *$/.test(item.align[i])) {
          item.align[i] = 'center';
        } else if (/^ *:-+ *$/.test(item.align[i])) {
          item.align[i] = 'left';
        } else {
          item.align[i] = null;
        }
      }

      for (i = 0; i < item.cells.length; i++) {
        item.cells[i] = item.cells[i].split(/ *\| */);
      }

      this.tokens.push(item);

      continue;
    }

    // lheading
    if (cap = this.rules.lheading.exec(src)) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'heading',
        depth: cap[2] === '=' ? 1 : 2,
        text: cap[1]
      });
      continue;
    }

    // hr
    if (cap = this.rules.hr.exec(src)) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'hr'
      });
      continue;
    }

    // blockquote
    if (cap = this.rules.blockquote.exec(src)) {
      src = src.substring(cap[0].length);

      this.tokens.push({
        type: 'blockquote_start'
      });

      cap = cap[0].replace(/^ *> ?/gm, '');

      // Pass `top` to keep the current
      // "toplevel" state. This is exactly
      // how markdown.pl works.
      this.token(cap, top);

      this.tokens.push({
        type: 'blockquote_end'
      });

      continue;
    }

    // list
    if (cap = this.rules.list.exec(src)) {
      src = src.substring(cap[0].length);
      bull = cap[2];

      this.tokens.push({
        type: 'list_start',
        ordered: bull.length > 1
      });

      // Get each top-level item.
      cap = cap[0].match(this.rules.item);

      next = false;
      l = cap.length;
      i = 0;

      for (; i < l; i++) {
        item = cap[i];

        // Remove the list item's bullet
        // so it is seen as the next token.
        space = item.length;
        item = item.replace(/^ *([*+-]|\d+\.) +/, '');

        // Outdent whatever the
        // list item contains. Hacky.
        if (~item.indexOf('\n ')) {
          space -= item.length;
          item = !this.options.pedantic
            ? item.replace(new RegExp('^ {1,' + space + '}', 'gm'), '')
            : item.replace(/^ {1,4}/gm, '');
        }

        // Determine whether the next list item belongs here.
        // Backpedal if it does not belong in this list.
        if (this.options.smartLists && i !== l - 1) {
          b = block.bullet.exec(cap[i+1])[0];
          if (bull !== b && !(bull.length > 1 && b.length > 1)) {
            src = cap.slice(i + 1).join('\n') + src;
            i = l - 1;
          }
        }

        // Determine whether item is loose or not.
        // Use: /(^|\n)(?! )[^\n]+\n\n(?!\s*$)/
        // for discount behavior.
        loose = next || /\n\n(?!\s*$)/.test(item);
        if (i !== l - 1) {
          next = item[item.length-1] === '\n';
          if (!loose) loose = next;
        }

        this.tokens.push({
          type: loose
            ? 'loose_item_start'
            : 'list_item_start'
        });

        // Recurse.
        this.token(item, false);

        this.tokens.push({
          type: 'list_item_end'
        });
      }

      this.tokens.push({
        type: 'list_end'
      });

      continue;
    }

    // html
    if (cap = this.rules.html.exec(src)) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: this.options.sanitize
          ? 'paragraph'
          : 'html',
        pre: cap[1] === 'pre',
        text: cap[0]
      });
      continue;
    }

    // def
    if (top && (cap = this.rules.def.exec(src))) {
      src = src.substring(cap[0].length);
      this.tokens.links[cap[1].toLowerCase()] = {
        href: cap[2],
        title: cap[3]
      };
      continue;
    }

    // table (gfm)
    if (top && (cap = this.rules.table.exec(src))) {
      src = src.substring(cap[0].length);

      item = {
        type: 'table',
        header: cap[1].replace(/^ *| *\| *$/g, '').split(/ *\| */),
        align: cap[2].replace(/^ *|\| *$/g, '').split(/ *\| */),
        cells: cap[3].replace(/(?: *\| *)?\n$/, '').split('\n')
      };

      for (i = 0; i < item.align.length; i++) {
        if (/^ *-+: *$/.test(item.align[i])) {
          item.align[i] = 'right';
        } else if (/^ *:-+: *$/.test(item.align[i])) {
          item.align[i] = 'center';
        } else if (/^ *:-+ *$/.test(item.align[i])) {
          item.align[i] = 'left';
        } else {
          item.align[i] = null;
        }
      }

      for (i = 0; i < item.cells.length; i++) {
        item.cells[i] = item.cells[i]
          .replace(/^ *\| *| *\| *$/g, '')
          .split(/ *\| */);
      }

      this.tokens.push(item);

      continue;
    }

    // top-level paragraph
    if (top && (cap = this.rules.paragraph.exec(src))) {
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'paragraph',
        text: cap[1][cap[1].length-1] === '\n'
          ? cap[1].slice(0, -1)
          : cap[1]
      });
      continue;
    }

    // text
    if (cap = this.rules.text.exec(src)) {
      // Top-level should never reach here.
      src = src.substring(cap[0].length);
      this.tokens.push({
        type: 'text',
        text: cap[0]
      });
      continue;
    }

    if (src) {
      throw new
        Error('Infinite loop on byte: ' + src.charCodeAt(0));
    }
  }

  return this.tokens;
};

/**
 * Inline-Level Grammar
 */

var inline = {
  escape: /^\\([\\`*{}\[\]()#+\-.!_>])/,
  autolink: /^<([^ >]+(@|:\/)[^ >]+)>/,
  url: noop,
  tag: /^<!--[\s\S]*?-->|^<\/?\w+(?:"[^"]*"|'[^']*'|[^'">])*?>/,
  link: /^!?\[(inside)\]\(href\)/,
  reflink: /^!?\[(inside)\]\s*\[([^\]]*)\]/,
  nolink: /^!?\[((?:\[[^\]]*\]|[^\[\]])*)\]/,
  strong: /^__([\s\S]+?)__(?!_)|^\*\*([\s\S]+?)\*\*(?!\*)/,
  em: /^\b_((?:__|[\s\S])+?)_\b|^\*((?:\*\*|[\s\S])+?)\*(?!\*)/,
  code: /^(`+)\s*([\s\S]*?[^`])\s*\1(?!`)/,
  br: /^ {2,}\n(?!\s*$)/,
  del: noop,
  text: /^[\s\S]+?(?=[\\<!\[_*`]| {2,}\n|$)/
};

inline._inside = /(?:\[[^\]]*\]|[^\]]|\](?=[^\[]*\]))*/;
inline._href = /\s*<?([^\s]*?)>?(?:\s+['"]([\s\S]*?)['"])?\s*/;

inline.link = replace(inline.link)
  ('inside', inline._inside)
  ('href', inline._href)
  ();

inline.reflink = replace(inline.reflink)
  ('inside', inline._inside)
  ();

/**
 * Normal Inline Grammar
 */

inline.normal = merge({}, inline);

/**
 * Pedantic Inline Grammar
 */

inline.pedantic = merge({}, inline.normal, {
  strong: /^__(?=\S)([\s\S]*?\S)__(?!_)|^\*\*(?=\S)([\s\S]*?\S)\*\*(?!\*)/,
  em: /^_(?=\S)([\s\S]*?\S)_(?!_)|^\*(?=\S)([\s\S]*?\S)\*(?!\*)/
});

/**
 * GFM Inline Grammar
 */

inline.gfm = merge({}, inline.normal, {
  escape: replace(inline.escape)('])', '~|])')(),
  url: /^(https?:\/\/[^\s<]+[^<.,:;"')\]\s])/,
  del: /^~~(?=\S)([\s\S]*?\S)~~/,
  text: replace(inline.text)
    (']|', '~]|')
    ('|', '|https?://|')
    ()
});

/**
 * GFM + Line Breaks Inline Grammar
 */

inline.breaks = merge({}, inline.gfm, {
  br: replace(inline.br)('{2,}', '*')(),
  text: replace(inline.gfm.text)('{2,}', '*')()
});

/**
 * Inline Lexer & Compiler
 */

function InlineLexer(links, options) {
  this.options = options || marked.defaults;
  this.links = links;
  this.rules = inline.normal;

  if (!this.links) {
    throw new
      Error('Tokens array requires a `links` property.');
  }

  if (this.options.gfm) {
    if (this.options.breaks) {
      this.rules = inline.breaks;
    } else {
      this.rules = inline.gfm;
    }
  } else if (this.options.pedantic) {
    this.rules = inline.pedantic;
  }
}

/**
 * Expose Inline Rules
 */

InlineLexer.rules = inline;

/**
 * Static Lexing/Compiling Method
 */

InlineLexer.output = function(src, links, options) {
  var inline = new InlineLexer(links, options);
  return inline.output(src);
};

/**
 * Lexing/Compiling
 */

InlineLexer.prototype.output = function(src) {
  var out = ''
    , link
    , text
    , href
    , cap;

  while (src) {
    // escape
    if (cap = this.rules.escape.exec(src)) {
      src = src.substring(cap[0].length);
      out += cap[1];
      continue;
    }

    // autolink
    if (cap = this.rules.autolink.exec(src)) {
      src = src.substring(cap[0].length);
      if (cap[2] === '@') {
        text = cap[1][6] === ':'
          ? this.mangle(cap[1].substring(7))
          : this.mangle(cap[1]);
        href = this.mangle('mailto:') + text;
      } else {
        text = escape(cap[1]);
        href = text;
      }
      out += '<a href="'
        + href
        + '">'
        + text
        + '</a>';
      continue;
    }

    // url (gfm)
    if (cap = this.rules.url.exec(src)) {
      src = src.substring(cap[0].length);
      text = escape(cap[1]);
      href = text;
      out += '<a href="'
        + href
        + '">'
        + text
        + '</a>';
      continue;
    }

    // tag
    if (cap = this.rules.tag.exec(src)) {
      src = src.substring(cap[0].length);
      out += this.options.sanitize
        ? escape(cap[0])
        : cap[0];
      continue;
    }

    // link
    if (cap = this.rules.link.exec(src)) {
      src = src.substring(cap[0].length);
      out += this.outputLink(cap, {
        href: cap[2],
        title: cap[3]
      });
      continue;
    }

    // reflink, nolink
    if ((cap = this.rules.reflink.exec(src))
        || (cap = this.rules.nolink.exec(src))) {
      src = src.substring(cap[0].length);
      link = (cap[2] || cap[1]).replace(/\s+/g, ' ');
      link = this.links[link.toLowerCase()];
      if (!link || !link.href) {
        out += cap[0][0];
        src = cap[0].substring(1) + src;
        continue;
      }
      out += this.outputLink(cap, link);
      continue;
    }

    // strong
    if (cap = this.rules.strong.exec(src)) {
      src = src.substring(cap[0].length);
      out += '<strong>'
        + this.output(cap[2] || cap[1])
        + '</strong>';
      continue;
    }

    // em
    if (cap = this.rules.em.exec(src)) {
      src = src.substring(cap[0].length);
      out += '<em>'
        + this.output(cap[2] || cap[1])
        + '</em>';
      continue;
    }

    // code
    if (cap = this.rules.code.exec(src)) {
      src = src.substring(cap[0].length);
      out += '<code>'
        + escape(cap[2], true)
        + '</code>';
      continue;
    }

    // br
    if (cap = this.rules.br.exec(src)) {
      src = src.substring(cap[0].length);
      out += '<br>';
      continue;
    }

    // del (gfm)
    if (cap = this.rules.del.exec(src)) {
      src = src.substring(cap[0].length);
      out += '<del>'
        + this.output(cap[1])
        + '</del>';
      continue;
    }

    // text
    if (cap = this.rules.text.exec(src)) {
      src = src.substring(cap[0].length);
      out += escape(cap[0]);
      continue;
    }

    if (src) {
      throw new
        Error('Infinite loop on byte: ' + src.charCodeAt(0));
    }
  }

  return out;
};

/**
 * Compile Link
 */

InlineLexer.prototype.outputLink = function(cap, link) {
  if (cap[0][0] !== '!') {
    return '<a href="'
      + escape(link.href)
      + '"'
      + (link.title
      ? ' title="'
      + escape(link.title)
      + '"'
      : '')
      + '>'
      + this.output(cap[1])
      + '</a>';
  } else {
    return '<img src="'
      + escape(link.href)
      + '" alt="'
      + escape(cap[1])
      + '"'
      + (link.title
      ? ' title="'
      + escape(link.title)
      + '"'
      : '')
      + '>';
  }
};

/**
 * Mangle Links
 */

InlineLexer.prototype.mangle = function(text) {
  var out = ''
    , l = text.length
    , i = 0
    , ch;

  for (; i < l; i++) {
    ch = text.charCodeAt(i);
    if (Math.random() > 0.5) {
      ch = 'x' + ch.toString(16);
    }
    out += '&#' + ch + ';';
  }

  return out;
};

/**
 * Parsing & Compiling
 */

function Parser(options) {
  this.tokens = [];
  this.token = null;
  this.options = options || marked.defaults;
}

/**
 * Static Parse Method
 */

Parser.parse = function(src, options) {
  var parser = new Parser(options);
  return parser.parse(src);
};

/**
 * Parse Loop
 */

Parser.prototype.parse = function(src) {
  this.inline = new InlineLexer(src.links, this.options);
  this.tokens = src.reverse();

  var out = '';
  while (this.next()) {
    out += this.tok();
  }

  return out;
};

/**
 * Next Token
 */

Parser.prototype.next = function() {
  return this.token = this.tokens.pop();
};

/**
 * Preview Next Token
 */

Parser.prototype.peek = function() {
  return this.tokens[this.tokens.length-1] || 0;
};

/**
 * Parse Text Tokens
 */

Parser.prototype.parseText = function() {
  var body = this.token.text;

  while (this.peek().type === 'text') {
    body += '\n' + this.next().text;
  }

  return this.inline.output(body);
};

/**
 * Parse Current Token
 */

Parser.prototype.tok = function() {
  switch (this.token.type) {
    case 'space': {
      return '';
    }
    case 'hr': {
      return '<hr>\n';
    }
    case 'heading': {
      return '<h'
        + this.token.depth
        + '>'
        + this.inline.output(this.token.text)
        + '</h'
        + this.token.depth
        + '>\n';
    }
    case 'code': {
      if (this.options.highlight) {
        var code = this.options.highlight(this.token.text, this.token.lang);
        if (code != null && code !== this.token.text) {
          this.token.escaped = true;
          this.token.text = code;
        }
      }

      if (!this.token.escaped) {
        this.token.text = escape(this.token.text, true);
      }
      return '<pre class="prettyprint"><code'
        + (this.token.lang
        ? ' class="'
        + this.options.langPrefix
        + this.token.lang
        + '"'
        : '')
        + '>'
        + this.token.text
        + '</code></pre>\n';
    }
    case 'table': {
      var body = ''
        , heading
        , i
        , row
        , cell
        , j;

      // header
      body += '<thead>\n<tr>\n';
      for (i = 0; i < this.token.header.length; i++) {
        heading = this.inline.output(this.token.header[i]);
        body += this.token.align[i]
          ? '<th align="' + this.token.align[i] + '">' + heading + '</th>\n'
          : '<th>' + heading + '</th>\n';
      }
      body += '</tr>\n</thead>\n';

      // body
      body += '<tbody>\n'
      for (i = 0; i < this.token.cells.length; i++) {
        row = this.token.cells[i];
        body += '<tr>\n';
        for (j = 0; j < row.length; j++) {
          cell = this.inline.output(row[j]);
          body += this.token.align[j]
            ? '<td align="' + this.token.align[j] + '">' + cell + '</td>\n'
            : '<td>' + cell + '</td>\n';
        }
        body += '</tr>\n';
      }
      body += '</tbody>\n';

      return '<table>\n'
        + body
        + '</table>\n';
    }
    case 'blockquote_start': {
      var body = '';

      while (this.next().type !== 'blockquote_end') {
        body += this.tok();
      }

      return '<blockquote>\n'
        + body
        + '</blockquote>\n';
    }
    case 'list_start': {
      var type = this.token.ordered ? 'ol' : 'ul'
        , body = '';

      while (this.next().type !== 'list_end') {
        body += this.tok();
      }

      return '<'
        + type
        + '>\n'
        + body
        + '</'
        + type
        + '>\n';
    }
    case 'list_item_start': {
      var body = '';

      while (this.next().type !== 'list_item_end') {
        body += this.token.type === 'text'
          ? this.parseText()
          : this.tok();
      }

      return '<li>'
        + body
        + '</li>\n';
    }
    case 'loose_item_start': {
      var body = '';

      while (this.next().type !== 'list_item_end') {
        body += this.tok();
      }

      return '<li>'
        + body
        + '</li>\n';
    }
    case 'html': {
      return !this.token.pre && !this.options.pedantic
        ? this.inline.output(this.token.text)
        : this.token.text;
    }
    case 'paragraph': {
      return '<p>'
        + this.inline.output(this.token.text)
        + '</p>\n';
    }
    case 'text': {
      return '<p>'
        + this.parseText()
        + '</p>\n';
    }
  }
};

/**
 * Helpers
 */

function escape(html, encode) {
  return html
    .replace(!encode ? /&(?!#?\w+;)/g : /&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

function replace(regex, opt) {
  regex = regex.source;
  opt = opt || '';
  return function self(name, val) {
    if (!name) return new RegExp(regex, opt);
    val = val.source || val;
    val = val.replace(/(^|[^\[])\^/g, '$1');
    regex = regex.replace(name, val);
    return self;
  };
}

function noop() {}
noop.exec = noop;

function merge(obj) {
  var i = 1
    , target
    , key;

  for (; i < arguments.length; i++) {
    target = arguments[i];
    for (key in target) {
      if (Object.prototype.hasOwnProperty.call(target, key)) {
        obj[key] = target[key];
      }
    }
  }

  return obj;
}

/**
 * Marked
 */

function marked(src, opt) {
  try {
    if (opt) opt = merge({}, marked.defaults, opt);
    return Parser.parse(Lexer.lex(src, opt), opt);
  } catch (e) {
    e.message += '\nPlease report this to https://github.com/chjj/marked.';
    if ((opt || marked.defaults).silent) {
      return '<p>An error occured:</p><pre>'
        + escape(e.message + '', true)
        + '</pre>';
    }
    throw e;
  }
}

/**
 * Options
 */

marked.options =
marked.setOptions = function(opt) {
  merge(marked.defaults, opt);
  return marked;
};

marked.defaults = {
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: false,
  smartLists: false,
  silent: false,
  highlight: null,
  langPrefix: 'lang-'
};

/**
 * Expose
 */

marked.Parser = Parser;
marked.parser = Parser.parse;

marked.Lexer = Lexer;
marked.lexer = Lexer.lex;

marked.InlineLexer = InlineLexer;
marked.inlineLexer = InlineLexer.output;

marked.parse = marked;

if (typeof exports === 'object') {
  module.exports = marked;
} else if (typeof define === 'function' && define.amd) {
  define(function() { return marked; });
} else {
  this.marked = marked;
}

}).call(function() {
  return this || (typeof window !== 'undefined' ? window : global);
}());
(function(){var a=(function(){var a={};var b=function(b,c,d){var e=b.split(".");for(var f=0;f<e.length-1;f++){if(!d[e[f]])d[e[f]]={};d=d[e[f]];}if(typeof c==="function")if(c.isClass)d[e[f]]=c;else d[e[f]]=function(){return c.apply(a,arguments);};else d[e[f]]=c;};var c=function(c,d,e){b(c,d,a);if(e)b(c,d,window.filepicker);};var d=function(b,d,e){if(typeof b==="function"){e=d;d=b;b='';}if(b)b+=".";var f=d.call(a);for(var g in f)c(b+g,f[g],e);};var e=function(b){b.apply(a,arguments);};return{extend:d,internal:e};})();if(!window.filepicker)window.filepicker=a;else for(attr in a)window.filepicker[attr]=a[attr];})();filepicker.extend("ajax",function(){var a=this;var b=function(a,b){b.method='GET';f(a,b);};var c=function(b,c){c.method='POST';b+=(b.indexOf('?')>=0?'&':'?')+'_cacheBust='+a.util.getId();f(b,c);};var d=function(b,c){var e=[];for(var f in b){var g=b[f];if(c)f=c+'['+f+']';var h;switch(a.util.typeOf(g)){case 'object':h=d(g,f);break;case 'array':var i={};for(var j=0;j<g.length;j++)i[j]=g[j];h=d(i,f);break;default:h=f+'='+encodeURIComponent(g);break;}if(g!==null)e.push(h);}return e.join('&');};var e=function(){try{return new window.XMLHttpRequest();}catch(a){try{return new window.ActiveXObject("Msxml2.XMLHTTP");}catch(a){try{return new window.ActiveXObject("Microsoft.XMLHTTP");}catch(a){return null;}}}};var f=function(b,c){b=b||"";var f=c.method?c.method.toUpperCase():"POST";var h=c.success||function(){};var i=c.error||function(){};var j=c.async===undefined?true:c.async;var k=c.data||null;var l=c.processData===undefined?true:c.processData;var m=c.headers||{};var n=a.util.parseUrl(b);var o=window.location.protocol+'//'+window.location.host;var p=o!==n.origin;var q=false;if(k&&l)k=d(c.data);var r;if(c.xhr)r=c.xhr;else{r=e();if(!r){c.error("Ajax not allowed");return r;}}if(p&&window.XDomainRequest&&!("withCredentials" in r))return g(b,c);if(c.progress&&r.upload)r.upload.addEventListener("progress",function(a){if(a.lengthComputable)c.progress(Math.round((a.loaded*95)/a.total));},false);var s=function(){if(r.readyState==4&&!q){if(c.progress)c.progress(100);if(r.status>=200&&r.status<300){var b=r.responseText;if(c.json)try{b=a.json.decode(b);}catch(d){t.call(r,"Invalid json: "+b);return;}h(b,r.status,r);q=true;}else{t.call(r,r.responseText);q=true;}}};r.onreadystatechange=s;var t=function(a){if(q)return;if(c.progress)c.progress(100);q=true;if(this.status==400){i("bad_params",this.status,this);return;}else if(this.status==403){i("not_authorized",this.status,this);return;}else if(this.status==404){i("not_found",this.status,this);return;}if(p)if(this.readyState==4&&this.status===0){i("CORS_not_allowed",this.status,this);return;}else{i("CORS_error",this.status,this);return;}i(a,this.status,this);};r.onerror=t;if(k&&f=='GET'){b+=(b.indexOf('?')!=-1?'&':'?')+k;k=null;}r.open(f,b,j);if(c.json)r.setRequestHeader('Accept','application/json, text/javascript');else r.setRequestHeader('Accept','text/javascript, text/html, application/xml, text/xml, */*');var u=m['Content-Type']||m['content-type'];if(k&&l&&(f=="POST"||f=="PUT")&&u==undefined)r.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=utf-8');if(m)for(var v in m)r.setRequestHeader(v,m[v]);r.send(k);return r;};var g=function(b,c){if(!window.XDomainRequest)return null;var e=c.method?c.method.toUpperCase():"POST";var f=c.success||function(){};var g=c.error||function(){};var h=c.data||{};if(window.location.protocol=="http:")b=b.replace("https:","http:");else if(window.location.protocol=="https:")b=b.replace("http:","https:");if(c.async)throw new a.FilepickerException("Asyncronous Cross-domain requests are not supported");if(e!="GET"&&e!="POST"){h._method=e;e="POST";}if(c.processData!==false)h=h?d(h):null;if(h&&e=='GET'){b+=(b.indexOf('?')>=0?'&':'?')+h;h=null;}b+=(b.indexOf('?')>=0?'&':'?')+'_xdr=true&_cacheBust='+a.util.getId();var i=new window.XDomainRequest();i.onload=function(){var b=i.responseText;if(c.progress)c.progress(100);if(c.json)try{b=a.json.decode(b);}catch(d){g("Invalid json: "+b,200,i);return;}f(b,200,i);};i.onerror=function(){if(c.progress)c.progress(100);g(i.responseText||"CORS_error",this.status||500,this);};i.onprogress=function(){};i.ontimeout=function(){};i.timeout=30000;i.open(e,b,true);i.send(h);return i;};return{get:b,post:c,request:f};});filepicker.extend("base64",function(){var a=this;_keyStr="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";var b=function(a,b){b=b||b===undefined;var c="";var e,f,g,h,i,j,k;var l=0;if(b)a=d(a);while(l<a.length){e=a.charCodeAt(l);f=a.charCodeAt(l+1);g=a.charCodeAt(l+2);l+=3;h=e>>2;i=((e&3)<<4)|(f>>4);j=((f&15)<<2)|(g>>6);k=g&63;if(isNaN(f))j=k=64;else if(isNaN(g))k=64;c=c+_keyStr.charAt(h)+_keyStr.charAt(i)+_keyStr.charAt(j)+_keyStr.charAt(k);}return c;};var c=function(a,b){b=b||b===undefined;var c="";var d,f,g;var h,i,j,k;var l=0;a=a.replace(/[^A-Za-z0-9\+\/\=]/g,"");while(l<a.length){h=_keyStr.indexOf(a.charAt(l));i=_keyStr.indexOf(a.charAt(l+1));j=_keyStr.indexOf(a.charAt(l+2));k=_keyStr.indexOf(a.charAt(l+3));l+=4;d=(h<<2)|(i>>4);f=((i&15)<<4)|(j>>2);g=((j&3)<<6)|k;c=c+String.fromCharCode(d);if(j!=64)c=c+String.fromCharCode(f);if(k!=64)c=c+String.fromCharCode(g);}if(b)c=e(c);return c;};var d=function(a){a=a.replace(/\r\n/g,"\n");var b="";for(var c=0;c<a.length;c++){var d=a.charCodeAt(c);if(d<128)b+=String.fromCharCode(d);else if((d>127)&&(d<2048)){b+=String.fromCharCode((d>>6)|192);b+=String.fromCharCode((d&63)|128);}else{b+=String.fromCharCode((d>>12)|224);b+=String.fromCharCode(((d>>6)&63)|128);b+=String.fromCharCode((d&63)|128);}}return b;};var e=function(a){var b="";var c=0;var d=c1=c2=0;while(c<a.length){d=a.charCodeAt(c);if(d<128){b+=String.fromCharCode(d);c++;}else if((d>191)&&(d<224)){c2=a.charCodeAt(c+1);b+=String.fromCharCode(((d&31)<<6)|(c2&63));c+=2;}else{c2=a.charCodeAt(c+1);c3=a.charCodeAt(c+2);b+=String.fromCharCode(((d&15)<<12)|((c2&63)<<6)|(c3&63));c+=3;}}return b;};return{encode:b,decode:c};},true);filepicker.extend("browser",function(){var a=this;var b=function(){return !!(navigator.userAgent.match(/iPhone/i)||navigator.userAgent.match(/iPod/i)||navigator.userAgent.match(/iPad/i));};var c=function(){return !!navigator.userAgent.match(/Android/i);};var d=function(){return !!navigator.userAgent.match(/MSIE 7\.0/i);};return{isIOS:b,isAndroid:c,isIE7:d};});filepicker.extend("comm",function(){var a=this;var b="filepicker_comm_iframe";var c=function(){if(window.frames[b]===undefined){f();var c;c=document.createElement("iframe");c.id=c.name=b;c.src=a.urls.COMM;c.style.display='none';document.body.appendChild(c);}};var d=function(b){if(b.origin!=a.urls.BASE)return;var c=a.json.parse(b.data);a.handlers.run(c);};var e=false;var f=function(){if(e)return;else e=true;if(window.addEventListener)window.addEventListener("message",d,false);else if(window.attachEvent)window.attachEvent("onmessage",d);else throw new a.FilepickerException("Unsupported browser");};var g=function(){if(window.removeEventListener)window.removeEventListener("message",d,false);else if(window.attachEvent)window.detachEvent("onmessage",d);else throw new a.FilepickerException("Unsupported browser");if(!e)return;else e=false;var c=document.getElementsByName(b);for(var f=0;f<c.length;f++)c[f].parentNode.removeChild(c[f]);try{delete window.frames[b];}catch(g){}};return{openChannel:c,closeChannel:g};});filepicker.extend("comm_fallback",function(){var a=this;var b="filepicker_comm_iframe";var c="host_comm_iframe";var d="";var e=200;var f=function(){g();};var g=function(){if(window.frames[c]===undefined){var b;b=document.createElement("iframe");b.id=b.name=c;d=b.src=a.urls.constructHostCommFallback();b.style.display='none';var e=function(){d=b.contentWindow.location.href;h();};if(b.attachEvent)b.attachEvent('onload',e);else b.onload=e;document.body.appendChild(b);}};var h=function(){if(window.frames[b]===undefined){var c;c=document.createElement("iframe");c.id=c.name=b;c.src=a.urls.FP_COMM_FALLBACK+"?host_url="+encodeURIComponent(d);c.style.display='none';document.body.appendChild(c);}m();};var i=false;var j=undefined;var k="";var l=function(){var d=window.frames[b];if(!d)return;var e=d.frames[c];if(!e)return;var f=e.location.hash;if(f&&f.charAt(0)=="#")f=f.substr(1);if(f===k)return;k=f;if(!k)return;var g;try{g=a.json.parse(f);}catch(h){}if(g)a.handlers.run(g);};var m=function(){if(i)return;else i=true;j=window.setInterval(l,e);};var n=function(){window.clearInterval(j);if(!i)return;else i=false;var a=document.getElementsByName(b);for(var d=0;d<a.length;d++)a[d].parentNode.removeChild(a[d]);try{delete window.frames[b];}catch(e){}a=document.getElementsByName(c);for(d=0;d<a.length;d++)a[d].parentNode.removeChild(a[d]);try{delete window.frames[c];}catch(e){}};var o=!('postMessage' in window);var p=function(a){if(a!==o){o=!!a;if(o)r();else s();}};var q;var r=function(){q=a.comm;a.comm={openChannel:f,closeChannel:n};};var s=function(){a.comm=q;q=undefined;};if(o)r();return{openChannel:f,closeChannel:n,isEnabled:o};});filepicker.extend("conversions",function(){var a=this;var b={width:'number',height:'number',fit:'string',format:'string',watermark:'string',watermark_size:'number',watermark_position:'string',align:'string',crop:'string or array',quality:'number',text:'string',text_font:'string',text_size:'number',text_color:'string',text_align:'string',text_padding:'number',policy:'string',signature:'string',storeLocation:'string',storePath:'string',storeAccess:'string',rotate:'string or number'};var c=function(c){var d;for(var e in c){d=false;for(var f in b)if(e==f){d=true;if(b[f].indexOf(a.util.typeOf(c[e]))===-1)throw new a.FilepickerException("Conversion parameter "+e+" is not the right type: "+c[e]+". Should be a "+b[f]);}if(!d)throw new a.FilepickerException("Conversion parameter "+e+" is not a valid parameter.");}};var d=function(b,d,e,f,g){c(d);if(d.crop&&a.util.isArray(d.crop))d.crop=d.crop.join(",");a.ajax.post(b+'/convert',{data:d,json:true,success:function(b){e(a.util.standardizeFPFile(b));},error:function(b,c,d){if(b=="not_found")f(new a.errors.FPError(141));else if(b=="bad_params")f(new a.errors.FPError(142));else if(b=="not_authorized")f(new a.errors.FPError(403));else f(new a.errors.FPError(143));},progress:g});};return{convert:d};});filepicker.extend("cookies",function(){var a=this;var b=function(b){var c=function(c){if(c.type!=="ThirdPartyCookies")return;a.cookies.THIRD_PARTY_COOKIES=!!c.payload;if(b&&typeof b==="function")b(!!c.payload);};return c;};var c=function(c){var d=b(c);a.handlers.attach('cookies',d);a.comm.openChannel();};return{checkThirdParty:c};});filepicker.extend("dragdrop",function(){var a=this;var b=function(){return(!!window.FileReader||navigator.userAgent.indexOf("Safari")>=0)&&('draggable' in document.createElement('span'));};var c=function(c,d){var e="No DOM element found to create drop pane";if(!c)throw new a.FilepickerException(e);if(c.jquery){if(c.length===0)throw new a.FilepickerException(e);c=c[0];}if(!b()){a.util.console.error("Your browser doesn't support drag-drop functionality");return false;}d=d||{};var f=d.dragEnter||function(){};var g=d.dragLeave||function(){};var h=d.onStart||function(){};var i=d.onSuccess||function(){};var j=d.onError||function(){};var k=d.onProgress||function(){};var l=d.mimetypes;if(!l)if(d.mimetype)l=[d.mimetype];else l=["*/*"];if(a.util.typeOf(l)=='string')l=l.split(',');var m=d.extensions;if(!m)if(d.extension)m=[d.extensions];if(a.util.typeOf(m)=='string')m=m.split(',');var n={location:d.location,path:d.path,access:d.access,policy:d.policy,signature:d.signature};c.addEventListener("dragenter",function(a){f();a.stopPropagation();a.preventDefault();return false;},false);c.addEventListener("dragleave",function(a){g();a.stopPropagation();a.preventDefault();return false;},false);c.addEventListener("dragover",function(a){a.preventDefault();return false;},false);c.addEventListener("drop",function(b){b.stopPropagation();b.preventDefault();var c;var d;var e;if(b.dataTransfer.items){d=b.dataTransfer.items;for(c=0;c<d.length;c++){e=d[c]&&d[c].webkitGetAsEntry?d[c].webkitGetAsEntry():undefined;if(e&&!!e.isDirectory){j("WrongType","Uploading a folder is not allowed");return;}}}var f=b.dataTransfer.files;var g=f.length;if(u(f)){h(f);for(c=0;c<f.length;c++)a.store(f[c],n,q(c,g),r,s(c,g));}});var o={};var p=[];var q=function(a,b){return function(c){if(!d.multiple)i([c]);else{p.push(c);if(p.length==b){i(p);p=[];}else{o[a]=100;t(b);}}};};var r=function(a){j("UploadError",a.toString());};var s=function(a,b){return function(c){o[a]=c;t(b);};};var t=function(a){var b=0;for(var c in o)b+=o[c];var d=b/a;k(d);};var u=function(b){if(b.length>0){if(b.length>1&&!d.multiple){j("TooManyFiles","Only one file at a time");return false;}var c;var e;var f;for(var g=0;g<b.length;g++){c=false;e=b[g];f=e.name||e.fileName||'"Unknown file"';for(var h=0;h<l.length;h++){var i=a.mimetypes.getMimetype(e);c=c||a.mimetypes.matchesMimetype(i,l[h]);}if(!c){j("WrongType",f+" isn't the right type of file");return false;}if(m){c=false;for(h=0;h<m.length;h++)c=c||a.util.endsWith(f,m[h]);if(!c){j("WrongType",f+" isn't the right type of file");return false;}}if(e.size&&!!d.maxSize&&e.size>d.maxSize){j("WrongSize",f+" is too large ("+e.size+" Bytes)");return false;}}return true;}else j("NoFilesFound","No files uploaded");return false;};return true;};return{enabled:b,makeDropPane:c};});filepicker.extend("errors",function(){var a=this;var b=function(a){if(this===window)return new b(a);this.code=a;if(filepicker.debug){var c=filepicker.error_map[this.code];this.message=c.message;this.moreInfo=c.moreInfo;this.toString=function(){return "FPError "+this.code+": "+this.message+". For help, see "+this.moreInfo;};}else this.toString=function(){return "FPError "+this.code+". Include filepicker_debug.js for more info";};return this;};b.isClass=true;var c=function(b){if(filepicker.debug)a.util.console.error(b.toString());};return{FPError:b,handleError:c};},true);filepicker.extend("exporter",function(){var a=this;var b=function(b){var c=function(c,d,e){if(b[d]&&!a.util.isArray(b[d]))b[d]=[b[d]];else if(b[c])b[d]=[b[c]];else if(e)b[d]=e;};if(b.mimetype&&b.extension)throw a.FilepickerException("Error: Cannot pass in both mimetype and extension parameters to the export function");c('service','services');if(b.services)for(var d=0;d<b.services.length;d++){var e=(''+b.services[d]).replace(" ","");var f=a.services[e];b.services[d]=(f===undefined?e:f);}if(b.openTo)b.openTo=a.services[b.openTo]||b.openTo;a.util.setDefault(b,'container','modal');};var c=function(b,c){var d=function(d){if(d.type!=="filepickerUrl")return;if(d.error){a.util.console.error(d.error);c(a.errors.FPError(132));}else{var e={};e.url=d.payload.url;e.filename=d.payload.data.filename;e.mimetype=d.payload.data.type;e.size=d.payload.data.size;e.isWriteable=true;b(e);}a.modal.close();};return d;};var d=function(e,f,g,h){b(f);if(f.debug){setTimeout(function(){g({url:"https://www.filepicker.io/api/file/-nBq2onTSemLBxlcBWn1",filename:'test.png',mimetype:'image/png',size:58979});},1);return;}if(a.cookies.THIRD_PARTY_COOKIES===undefined){a.cookies.checkThirdParty(function(){d(e,f,g,h);});return;}var i=a.util.getId();var j=false;var k=function(a){j=true;g(a);};var l=function(a){j=true;h(a);};var m=function(){if(!j){j=true;h(a.errors.FPError(131));}};if(f.container=='modal'&&(f.mobile||a.window.shouldForce()))f.container='window';a.window.open(f.container,a.urls.constructExportUrl(e,f,i),m);a.handlers.attach(i,c(k,l));};return{createExporter:d};});filepicker.extend("files",function(){var a=this;var b=function(b,d,e,f,g){var h=d.base64encode===undefined;if(h)d.base64encode=true;d.base64encode=d.base64encode!==false;var i=function(b){if(h)b=a.base64.decode(b,!!d.asText);e(b);};c.call(this,b,d,i,f,g);};var c=function(b,c,d,e,f){if(c.cache!==true)c._cacheBust=a.util.getId();a.ajax.get(b,{data:c,headers:{'X-NO-STREAM':true},success:d,error:function(b,c,d){if(b=="CORS_not_allowed")e(new a.errors.FPError(113));else if(b=="CORS_error")e(new a.errors.FPError(114));else if(b=="not_found")e(new a.errors.FPError(115));else if(b=="bad_params")e(new a.errors.FPError(400));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(118));},progress:f});};var d=function(b,c,d,e,f){if(!(window.File&&window.FileReader&&window.FileList&&window.Blob)){f(10);a.files.storeFile(b,{},function(b){f(50);a.files.readFromFPUrl(b.url,c,d,e,function(a){f(50+a/2);});},e,function(a){f(a/2);});return;}var g=!!c.base64encode;var h=!!c.asText;var i=new FileReader();i.onprogress=function(a){if(a.lengthComputable)f(Math.round((a.loaded/a.total)*100));};i.onload=function(b){f(100);if(g)d(a.base64.encode(b.target.result,h));else d(b.target.result);};i.onerror=function(b){switch(b.target.error.code){case b.target.error.NOT_FOUND_ERR:e(new a.errors.FPError(115));break;case b.target.error.NOT_READABLE_ERR:e(new a.errors.FPError(116));break;case b.target.error.ABORT_ERR:e(new a.errors.FPError(117));break;default:e(new a.errors.FPError(118));break;}};if(h||!i.readAsBinaryString)i.readAsText(b);else i.readAsBinaryString(b);};var e=function(b,c,d,e,f,g){var h=d.mimetype||'text/plain';a.ajax.post(a.urls.constructWriteUrl(b,d),{headers:{'Content-Type':h},data:c,processData:false,json:true,success:function(b){e(a.util.standardizeFPFile(b));},error:function(b,c,d){if(b=="not_found")f(new a.errors.FPError(121));else if(b=="bad_params")f(new a.errors.FPError(122));else if(b=="not_authorized")f(new a.errors.FPError(403));else f(new a.errors.FPError(123));},progress:g});};var f=function(b,c,d,e,f,g){var h=function(b,c,d){if(b=="not_found")f(new a.errors.FPError(121));else if(b=="bad_params")f(new a.errors.FPError(122));else if(b=="not_authorized")f(new a.errors.FPError(403));else f(new a.errors.FPError(123));};var i=function(b){e(a.util.standardizeFPFile(b));};k(c,a.urls.constructWriteUrl(b,d),i,h,g);};var g=function(b,c,d,e,f,g){var h=function(b,c,d){if(b=="not_found")f(new a.errors.FPError(121));else if(b=="bad_params")f(new a.errors.FPError(122));else if(b=="not_authorized")f(new a.errors.FPError(403));else f(new a.errors.FPError(123));};var i=function(b){e(a.util.standardizeFPFile(b));};d.mimetype=c.type;k(c,a.urls.constructWriteUrl(b,d),i,h,g);};var h=function(b,c,d,e,f,g){a.ajax.post(a.urls.constructWriteUrl(b,d),{data:{'url':c},json:true,success:function(b){e(a.util.standardizeFPFile(b));},error:function(b,c,d){if(b=="not_found")f(new a.errors.FPError(121));else if(b=="bad_params")f(new a.errors.FPError(122));else if(b=="not_authorized")f(new a.errors.FPError(403));else f(new a.errors.FPError(123));},progress:g});};var i=function(b,c,d,e,f){if(b.files){if(b.files.length===0)e(new a.errors.FPError(115));else j(b.files[0],c,d,e,f);return;}a.util.setDefault(c,'storage','S3');if(!c.filename)c.filename=b.value.replace("C:\\fakepath\\","")||b.name;var g=b.name;b.name="fileUpload";a.iframeAjax.post(a.urls.constructStoreUrl(c),{data:b,processData:false,json:true,success:function(c){b.name=g;d(a.util.standardizeFPFile(c));},error:function(b,c,d){if(b=="not_found")e(new a.errors.FPError(121));else if(b=="bad_params")e(new a.errors.FPError(122));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(123));}});};var j=function(b,c,d,e,f){a.util.setDefault(c,'storage','S3');var g=function(b,c,d){if(b=="not_found")e(new a.errors.FPError(121));else if(b=="bad_params")e(new a.errors.FPError(122));else if(b=="not_authorized")e(new a.errors.FPError(403));else{a.util.console.error(b);e(new a.errors.FPError(123));}};var h=function(b){d(a.util.standardizeFPFile(b));};if(!c.filename)c.filename=b.name||b.fileName;k(b,a.urls.constructStoreUrl(c),h,g,f);};var k=function(b,c,d,e,f){if(b.files)b=b.files[0];var g=!!window.FormData&&!!window.XMLHttpRequest;if(g){data=new FormData();data.append('fileUpload',b);a.ajax.post(c,{json:true,processData:false,data:data,success:d,error:e,progress:f});}else a.iframeAjax.post(c,{data:b,json:true,success:d,error:e});};var l=function(b,c,d,e,f){a.util.setDefault(c,'storage','S3');a.util.setDefault(c,'mimetype','text/plain');a.ajax.post(a.urls.constructStoreUrl(c),{headers:{'Content-Type':c.mimetype},data:b,processData:false,json:true,success:function(b){d(a.util.standardizeFPFile(b));},error:function(b,c,d){if(b=="not_found")e(new a.errors.FPError(121));else if(b=="bad_params")e(new a.errors.FPError(122));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(123));},progress:f});};var m=function(b,c,d,e,f){a.util.setDefault(c,'storage','S3');a.ajax.post(a.urls.constructStoreUrl(c),{data:{'url':b},json:true,success:function(b){d(a.util.standardizeFPFile(b));},error:function(b,c,d){if(b=="not_found")e(new a.errors.FPError(151));else if(b=="bad_params")e(new a.errors.FPError(152));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(153));},progress:f});};var n=function(b,c,d,e){var f=['uploaded','modified','created'];if(c.cache!==true)c._cacheBust=a.util.getId();a.ajax.get(b+"/metadata",{json:true,data:c,success:function(a){for(var b=0;b<f.length;b++)if(a[f[b]])a[f[b]]=new Date(a[f[b]]);d(a);},error:function(b,c,d){if(b=="not_found")e(new a.errors.FPError(161));else if(b=="bad_params")e(new a.errors.FPError(400));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(162));}});};var o=function(b,c,d,e){c.key=a.apikey;a.ajax.post(b+"/remove",{data:c,success:function(a){d();},error:function(b,c,d){if(b=="not_found")e(new a.errors.FPError(171));else if(b=="bad_params")e(new a.errors.FPError(400));else if(b=="not_authorized")e(new a.errors.FPError(403));else e(new a.errors.FPError(172));}});};return{readFromUrl:c,readFromFile:d,readFromFPUrl:b,writeDataToFPUrl:e,writeFileToFPUrl:g,writeFileInputToFPUrl:f,writeUrlToFPUrl:h,storeFileInput:i,storeFile:j,storeUrl:m,storeData:l,stat:n,remove:o};});filepicker.extend("handlers",function(){var a=this;var b={};var c=function(a,c){if(b.hasOwnProperty(a))b[a].push(c);else b[a]=[c];return c;};var d=function(a,c){var d=b[a];if(!d)return;if(c){for(var e=0;e<d.length;e++)if(d[e]===c){d.splice(e,1);break;}if(d.length===0)delete b[a];}else delete b[a];};var e=function(a){var c=a.id;if(b.hasOwnProperty(c)){var d=b[c];for(var e=0;e<d.length;e++)d[e](a);return true;}return false;};return{attach:c,detach:d,run:e};});filepicker.extend("iframeAjax",function(){var a=this;var b="ajax_iframe";var c=[];var d=false;var e=function(a,b){b.method='GET';h(a,b);};var f=function(b,c){c.method='POST';b+=(b.indexOf('?')>=0?'&':'?')+'_cacheBust='+a.util.getId();h(b,c);};var g=function(){if(c.length>0){var a=c.shift();h(a.url,a.options);}};var h=function(e,f){if(d){c.push({url:e,options:f});return;}e+=(e.indexOf('?')>=0?'&':'?')+'_cacheBust='+a.util.getId();e+="&Content-Type=text%2Fhtml";a.comm.openChannel();var g;try{g=document.createElement('<iframe name="'+b+'">');}catch(h){g=document.createElement('iframe');}g.id=g.name=b;g.style.display='none';var k=function(){d=false;};if(g.attachEvent){g.attachEvent("onload",k);g.attachEvent("onerror",k);}else g.onerror=g.onload=k;g.id=b;g.name=b;g.style.display='none';g.onerror=g.onload=function(){d=false;};document.body.appendChild(g);a.handlers.attach('upload',i(f));var l=document.createElement("form");l.method=f.method||'GET';l.action=e;l.target=b;var m=f.data;if(a.util.isFileInputElement(m)||a.util.isFile(m))l.encoding=l.enctype="multipart/form-data";document.body.appendChild(l);if(a.util.isFile(m)){var n=j(m);if(!n)throw a.FilepickerException("Couldn't find corresponding file input.");m={'fileUpload':n};}else if(a.util.isFileInputElement(m)){var o=m;m={};m.fileUpload=o;}else if(m&&a.util.isElement(m)&&m.tagName=="INPUT"){o=m;m={};m[o.name]=o;}else if(f.processData!==false)m={'data':m};m.format='iframe';var p={};for(var q in m){var r=m[q];if(a.util.isElement(r)&&r.tagName=="INPUT"){p[q]={par:r.parentNode,sib:r.nextSibling,name:r.name,input:r,focused:r==document.activeElement};r.name=q;l.appendChild(r);}else{var s=document.createElement("input");s.name=q;s.value=r;l.appendChild(s);}}d=true;window.setTimeout(function(){l.submit();for(var a in p){var b=p[a];b.par.insertBefore(b.input,b.sib);b.input.name=b.name;if(b.focused)b.input.focus();}l.parentNode.removeChild(l);},1);};var i=function(b){var c=b.success||function(){};var e=b.error||function(){};var f=function(b){if(b.type!=="Upload")return;d=false;var f=b.payload;if(f.error)e(f.error);else c(f);a.handlers.detach("upload");g();};return f;};var j=function(a){var b=document.getElementsByTagName("input");for(var c=0;c<b.length;c++){var d=b[0];if(d.type!="file"||!d.files||!d.files.length)continue;for(var e=0;e<d.files.length;e++)if(d.files[e]===a)return d;}return null;};return{get:e,post:f,request:h};});filepicker.extend("json",function(){var a=this;var b={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'};var c=function(a){return b[a]||'\\u'+('0000'+a.charCodeAt(0).toString(16)).slice(-4);};var d=function(a){a=a.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,'@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,']').replace(/(?:^|:|,)(?:\s*\[)+/g,'');return(/^[\],:{}\s]*$/).test(a);};var e=function(b){if(window.JSON&&window.JSON.stringify)return window.JSON.stringify(b);if(b&&b.toJSON)b=b.toJSON();var d=[];switch(a.util.typeOf(b)){case 'string':return '"'+b.replace(/[\x00-\x1f\\"]/g,c)+'"';case 'array':for(var f=0;f<b.length;f++)d.push(e(b[f]));return '['+d+']';case 'object':case 'hash':var g;var h;for(h in b){g=e(b[h]);if(g)d.push(e(h)+':'+g);g=null;}return '{'+d+'}';case 'number':case 'boolean':return ''+b;case 'null':return 'null';default:return 'null';}return null;};var f=function(b,c){if(!b||a.util.typeOf(b)!='string')return null;if(window.JSON&&window.JSON.parse)return window.JSON.parse(b);else{if(c)if(!d(b))throw new Error('JSON could not decode the input; security is enabled and the value is not secure.');return eval('('+b+')');}};return{validate:d,encode:e,stringify:e,decode:f,parse:f};});filepicker.extend(function(){var a=this;a.API_VERSION="v1";var b=function(b){a.apikey=b;};var c=function(a){this.text=a;this.toString=function(){return "FilepickerException: "+this.text;};return this;};c.isClass=true;var d=function(){if(!a.apikey)throw new a.FilepickerException("API Key not found");};var e=function(b,c,e){d();if(typeof b==="function"){e=c;c=b;b={};}b=b||{};c=c||function(){};e=e||a.errors.handleError;a.picker.createPicker(b,c,e,false);};var f=function(b,c,e){d();if(typeof b==="function"){e=c;c=b;b={};}b=b||{};c=c||function(){};e=e||a.errors.handleError;a.picker.createPicker(b,c,e,true);};var g=function(b,c,e,f){d();if(!b||!c||typeof b==="function"||typeof b==="function")throw new a.FilepickerException("Not all required parameters given, missing picker or store options");f=f||a.errors.handleError;var g=!!b.multiple;var h=!!b?a.util.clone(b):{};h.storeLocation=c.location||'S3';h.storePath=c.path;h.storeAccess=c.access||'private';if(g&&h.storePath)if(h.storePath.charAt(h.storePath.length-1)!="/")throw new a.FilepickerException("pickAndStore with multiple files requires a path that ends in '/'");var i=e;if(!g)i=function(a){e([a]);};a.picker.createPicker(h,i,f,g);};var h=function(b,c,e,f,g){d();if(!b)throw new a.FilepickerException("No input given - nothing to read!");if(typeof c==="function"){g=f;f=e;e=c;c={};}c=c||{};e=e||function(){};f=f||a.errors.handleError;g=g||function(){};if(typeof b=="string")if(a.util.isFPUrl(b))a.files.readFromFPUrl(b,c,e,f,g);else a.files.readFromUrl(b,c,e,f,g);else if(a.util.isFileInputElement(b))if(!b.files)i(b,c,e,f,g);else if(b.files.length===0)f(new a.errors.FPError(115));else a.files.readFromFile(b.files[0],c,e,f,g);else if(a.util.isFile(b))a.files.readFromFile(b,c,e,f,g);else if(b.url)a.files.readFromFPUrl(b.url,c,e,f,g);else throw new a.FilepickerException("Cannot read given input: "+b+". Not a url, file input, DOM File, or FPFile object.");};var i=function(b,c,d,e,f){f(10);a.store(b,function(b){f(50);a.read(b,c,d,e,function(a){f(50+a/2);});},e);};var j=function(b,c,e,f,g,h){d();if(!b)throw new a.FilepickerException("No fpfile given - nothing to write to!");if(c===undefined||c===null)throw new a.FilepickerException("No input given - nothing to write!");if(typeof e==="function"){h=g;g=f;f=e;e={};}e=e||{};f=f||function(){};g=g||a.errors.handleError;h=h||function(){};var i;if(a.util.isFPUrl(b))i=b;else if(b.url)i=b.url;else throw new a.FilepickerException("Invalid file to write to: "+b+". Not a filepicker url or FPFile object.");if(typeof c=="string")a.files.writeDataToFPUrl(i,c,e,f,g,h);else if(a.util.isFileInputElement(c))if(!c.files)a.files.writeFileInputToFPUrl(i,c,e,f,g,h);else if(c.files.length===0)g(new a.errors.FPError(115));else a.files.writeFileToFPUrl(i,c.files[0],e,f,g,h);else if(a.util.isFile(c))a.files.writeFileToFPUrl(i,c,e,f,g,h);else if(c.url)a.files.writeUrlToFPUrl(i,c.url,e,f,g,h);else throw new a.FilepickerException("Cannot read from given input: "+c+". Not a string, file input, DOM File, or FPFile object.");};var k=function(b,c,e,f,g,h){d();if(!b)throw new a.FilepickerException("No fpfile given - nothing to write to!");if(c===undefined||c===null)throw new a.FilepickerException("No input given - nothing to write!");if(typeof e==="function"){h=g;g=f;f=e;e={};}e=e||{};f=f||function(){};g=g||a.errors.handleError;h=h||function(){};var i;if(a.util.isFPUrl(b))i=b;else if(b.url)i=b.url;else throw new a.FilepickerException("Invalid file to write to: "+b+". Not a filepicker url or FPFile object.");a.files.writeUrlToFPUrl(i,c,e,f,g,h);};var l=function(b,c,e,f){d();if(typeof c==="function"){f=e;e=c;c={};}c=!!c?a.util.clone(c):{};e=e||function(){};f=f||a.errors.handleError;var g;if(typeof b=="string"&&a.util.isUrl(b))g=b;else if(b.url){g=b.url;if(!c.mimetype&&!c.extension)c.mimetype=b.mimetype;if(!c.suggestedFilename)c.suggestedFilename=b.filename;}else throw new a.FilepickerException("Invalid file to export: "+b+". Not a valid url or FPFile object. You may want to use filepicker.store() to get an FPFile to export");a.exporter.createExporter(g,c,e,f);};var m=function(b,c,e,f,g){d();if(typeof c==="function"){g=f;f=e;e=c;c={};}c=!!c?a.util.clone(c):{};e=e||function(){};f=f||a.errors.handleError;g=g||function(){};if(typeof b=="string")a.files.storeData(b,c,e,f,g);else if(a.util.isFileInputElement(b))if(!b.files)a.files.storeFileInput(b,c,e,f,g);else if(b.files.length===0)f(new a.errors.FPError(115));else a.files.storeFile(b.files[0],c,e,f,g);else if(a.util.isFile(b))a.files.storeFile(b,c,e,f,g);else if(b.url){if(!c.filename)c.filename=b.filename;a.files.storeUrl(b.url,c,e,f,g);}else throw new a.FilepickerException("Cannot store given input: "+b+". Not a string, file input, DOM File, or FPFile object.");};var n=function(b,c,e,f,g){d();if(typeof c==="function"){g=f;f=e;e=c;c={};}c=c||{};e=e||function(){};f=f||a.errors.handleError;g=g||function(){};a.files.storeUrl(b,c,e,f,g);};var o=function(b,c,e,f){d();if(typeof c==="function"){f=e;e=c;c={};}c=c||{};e=e||function(){};f=f||a.errors.handleError;var g;if(a.util.isFPUrl(b))g=b;else if(b.url)g=b.url;else throw new a.FilepickerException("Invalid file to get metadata for: "+b+". Not a filepicker url or FPFile object.");a.files.stat(g,c,e,f);};var p=function(b,c,e,f){d();if(typeof c==="function"){f=e;e=c;c={};}c=c||{};e=e||function(){};f=f||a.errors.handleError;var g;if(a.util.isFPUrl(b))g=b;else if(b.url)g=b.url;else throw new a.FilepickerException("Invalid file to remove: "+b+". Not a filepicker url or FPFile object.");a.files.remove(g,c,e,f);};var q=function(b,c,e,f,g,h){d();if(!b)throw new a.FilepickerException("No fpfile given - nothing to convert!");if(typeof e==="function"){h=g;g=f;f=e;e={};}options=!!c?a.util.clone(c):{};e=e||{};f=f||function(){};g=g||a.errors.handleError;h=h||function(){};if(e.location)options.storeLocation=e.location;if(e.path)options.storePath=e.path;options.storeAccess=e.access||'private';var i;if(a.util.isFPUrl(b))i=b;else if(b.url){i=b.url;if(!a.mimetypes.matchesMimetype(b.mimetype,'image/*')){g(new a.errors.FPError(142));return;}}else throw new a.FilepickerException("Invalid file to convert: "+b+". Not a filepicker url or FPFile object.");a.conversions.convert(i,options,f,g,h);};var r=function(b){return a.widgets.constructWidget(b);};var s=function(b,c){return a.dragdrop.makeDropPane(b,c);};return{setKey:b,pick:e,pickMultiple:f,pickAndStore:g,read:h,write:j,writeUrl:k,'export':l,exportFile:l,store:m,storeUrl:n,stat:o,metadata:o,remove:p,convert:q,constructWidget:r,makeDropPane:s,FilepickerException:c};},true);filepicker.extend('mimetypes',function(){var a=this;var b={'.stl':'applicaiton/sla','.hbs':'text/html','.pdf':'application/pdf','.jpg':'image/jpeg','.jpeg':'image/jpeg','.jpe':'image/jpeg','.imp':'application/x-impressionist'};var c=['application/octet-stream','application/download','application/force-download','octet/stream','application/unknown','application/x-download','application/x-msdownload','application/x-secure-download'];var d=function(a){if(a.type){var d=a.type;d=d.toLowerCase();var e=false;for(var f=0;f<c.length;f++)e=e||d==c[f];if(!e)return a.type;}var g=a.name||a.fileName;var h=g.match(/\.\w*$/);if(h)return b[h[0].toLowerCase()]||'';else if(a.type)return a.type;else return '';};var e=function(b,d){if(!b)return d=="*/*";b=a.util.trim(b).toLowerCase();d=a.util.trim(d).toLowerCase();for(var e=0;e<c.length;e++)if(b==c[e])return true;test_parts=b.split("/");against_parts=d.split("/");if(against_parts[0]=="*")return true;if(against_parts[0]!=test_parts[0])return false;if(against_parts[1]=="*")return true;return against_parts[1]==test_parts[1];};return{getMimetype:d,matchesMimetype:e};});filepicker.extend("modal",function(){var a=this;var b="filepicker_shade";var c="filepicker_dialog_container";var d=function(b,c){var d=e(c);var h=f();var i=g(c);var j=document.createElement("iframe");j.name=a.window.WINDOW_NAME;j.id=a.window.WINDOW_NAME;var k=a.window.getSize();var l=Math.min(k[1]-40,500);j.style.width='100%';j.style.height=l-32+'px';j.style.border="none";j.style.position="relative";j.setAttribute('border',0);j.setAttribute('frameborder',0);j.setAttribute('frameBorder',0);j.setAttribute('marginwidth',0);j.setAttribute('marginheight',0);j.src=b;document.body.appendChild(d);h.appendChild(i);h.appendChild(j);document.body.appendChild(h);return j;};var e=function(a){var c=document.createElement("div");c.id=b;c.style.position='fixed';c.style.top=0;c.style.bottom=0;c.style.right=0;c.style.left=0;c.style.backgroundColor='#000000';c.style.opacity='0.5';c.style.filter='alpha(opacity=50)';c.style.zIndex=10000;c.onclick=h(a);return c;};var f=function(){var b=document.createElement("div");b.id=c;b.style.position='fixed';b.style.padding="10px";b.style.background='#ffffff url("https://www.filepicker.io/static/img/spinner.gif") no-repeat 50% 50%';b.style.top='10px';b.style.bottom='auto';b.style.right='auto';var d=a.window.getSize();var e=Math.min(d[1]-40,500);var f=Math.max(Math.min(d[0]-40,800),620);var g=(d[0]-f-40)/2;b.style.left=g+"px";b.style.height=e+'px';b.style.width=f+'px';b.style.overflow='hidden';b.style.webkitOverflowScrolling='touch';b.style.border='1px solid #999';b.style.webkitBorderRadius='3px';b.style.borderRadius='3px';b.style.margin='0';b.style.webkitBoxShadow='0 3px 7px rgba(0, 0, 0, 0.3)';b.style.boxShadow='0 3px 7px rgba(0, 0, 0, 0.3)';b.style.zIndex=10001;b.style.boxSizing="content-box";b.style.webkitBoxSizing="content-box";b.style.mozBoxSizing="content-box";return b;};var g=function(a){var b=document.createElement("a");b.appendChild(document.createTextNode('\u00D7'));b.onclick=h(a);b.style.cssFloat="right";b.style.styleFloat="right";b.style.cursor="default";b.style.padding='0 5px 0 0px';b.style.fontSize='1.5em';b.style.color='#555555';b.style.textDecoration='none';return b;};var h=function(d,e){e=!!e;return function(){if(a.uploading&&!e)if(!window.confirm("You are currently uploading. If you choose 'OK', the window will close and your upload will not finish. Do you want to stop uploading and close the window?"))return;a.uploading=false;var f=document.getElementById(b);if(f)document.body.removeChild(f);var g=document.getElementById(c);if(g)document.body.removeChild(g);try{delete window.frames[a.window.WINDOW_NAME];}catch(h){}if(d)d();};};var i=h(function(){});return{generate:d,close:i};});filepicker.extend("picker",function(){var a=this;var b=function(b){var c=function(c,d,e){if(b[d]){if(!a.util.isArray(b[d]))b[d]=[b[d]];}else if(b[c])b[d]=[b[c]];else if(e)b[d]=e;};c('service','services');c('mimetype','mimetypes');c('extension','extensions');if(b.services)for(var d=0;d<b.services.length;d++){var e=(''+b.services[d]).replace(" ","");if(a.services[e]!==undefined)e=a.services[e];b.services[d]=e;}if(b.mimetypes&&b.extensions)throw a.FilepickerException("Error: Cannot pass in both mimetype and extension parameters to the pick function");if(!b.mimetypes&&!b.extensions)b.mimetypes=['*/*'];if(b.openTo)b.openTo=a.services[b.openTo]||b.openTo;a.util.setDefault(b,'container','modal');};var c=function(b,c){var d=function(d){if(d.type!=="filepickerUrl")return;a.uploading=false;if(d.error){a.util.console.error(d.error);c(a.errors.FPError(102));}else{var e={};e.url=d.payload.url;e.filename=d.payload.data.filename;e.mimetype=d.payload.data.type;e.size=d.payload.data.size;if(d.payload.data.key)e.key=d.payload.data.key;e.isWriteable=true;b(e);}a.modal.close();};return d;};var d=function(b){b=b||function(){};var c=function(c){if(c.type!=="uploading")return;a.uploading=!!c.payload;b(a.uploading);};return c;};var e=function(b,c){var d=function(d){if(d.type!=="filepickerUrl")return;a.uploading=false;if(d.error){a.util.console.error(d.error);c(a.errors.FPError(102));}else{var e=[];if(!a.util.isArray(d.payload))d.payload=[d.payload];for(var f=0;f<d.payload.length;f++){var g={};var h=d.payload[f];g.url=h.url;g.filename=h.data.filename;g.mimetype=h.data.type;g.size=h.data.size;if(h.data.key)g.key=h.data.key;g.isWriteable=true;e.push(g);}b(e);}a.modal.close();};return d;};var f=function(g,h,i,j){b(g);if(g.debug){setTimeout(function(){h({url:"https://www.filepicker.io/api/file/-nBq2onTSemLBxlcBWn1",filename:'test.png',mimetype:'image/png',size:58979});},1);return;}if(a.cookies.THIRD_PARTY_COOKIES===undefined){a.cookies.checkThirdParty(function(){f(g,h,i,!!j);});return;}var k=a.util.getId();var l=false;var m=function(a){l=true;h(a);};var n=function(a){l=true;i(a);};var o=function(){if(!l){l=true;i(a.errors.FPError(101));}};if(g.container=='modal'&&(g.mobile||a.window.shouldForce()))g.container='window';a.window.open(g.container,a.urls.constructPickUrl(g,k,j),o);var p=j?e(m,n):c(m,n);a.handlers.attach(k,p);var q=k+"-upload";a.handlers.attach(q,d(function(){a.handlers.detach(q);}));};return{createPicker:f};});filepicker.extend('services',function(){return{COMPUTER:1,DROPBOX:2,FACEBOOK:3,GITHUB:4,GMAIL:5,IMAGE_SEARCH:6,URL:7,WEBCAM:8,GOOGLE_DRIVE:9,SEND_EMAIL:10,INSTAGRAM:11,FLICKR:12,VIDEO:13,EVERNOTE:14,PICASA:15,WEBDAV:16,FTP:17,ALFRESCO:18,BOX:19,SKYDRIVE:20};},true);filepicker.extend('util',function(){var a=this;var b=function(a){return a.replace(/^\s+|\s+$/g,"");};var c=/^(http|https)\:.*\/\//i;var d=function(a){return !!a.match(c);};var e=function(a){if(!a||a.charAt(0)=='/')a=window.location.protocol+"//"+window.location.host+a;var b=document.createElement('a');b.href=a;var c=b.hostname.indexOf(":")==-1?b.hostname:b.host;var d={source:a,protocol:b.protocol.replace(':',''),host:c,port:b.port,query:b.search,params:(function(){var a={},c=b.search.replace(/^\?/,'').split('&'),d=c.length,e=0,f;for(;e<d;e++){if(!c[e])continue;f=c[e].split('=');a[f[0]]=f[1];}return a;})(),file:(b.pathname.match(/\/([^\/?#]+)$/i)||[,''])[1],hash:b.hash.replace('#',''),path:b.pathname.replace(/^([^\/])/,'/$1'),relative:(b.href.match(/tps?:\/\/[^\/]+(.+)/)||[,''])[1],segments:b.pathname.replace(/^\//,'').split('/')};d.origin=d.protocol+"://"+d.host+(d.port?":"+d.port:'');return d;};var f=function(a,b){return a.indexOf(b,a.length-b.length)!==-1;};return{trim:b,parseUrl:e,isUrl:d,endsWith:f};});filepicker.extend("urls",function(){var a=this;var b="https://www.filepicker.io";if(window.filepicker.hostname)b=window.filepicker.hostname;var c=b+"/dialog/open/";var d=b+"/dialog/save/";var e=b+"/api/store/";var f=function(b,d,e){return c+"?key="+a.apikey+"&id="+d+"&referrer="+window.location.hostname+"&iframe="+(b.container!='window')+"&version="+a.API_VERSION+(b.services?"&s="+b.services.join(","):"")+(e?"&multi="+!!e:"")+(b.mimetypes!==undefined?"&m="+b.mimetypes.join(","):"")+(b.extensions!==undefined?"&ext="+b.extensions.join(","):"")+(b.openTo!==undefined?"&loc="+b.openTo:"")+(b.maxSize?"&maxSize="+b.maxSize:"")+(b.signature?"&signature="+b.signature:"")+(b.policy?"&policy="+b.policy:"")+(b.mobile!==undefined?"&mobile="+b.mobile:"")+(b.storeLocation?"&storeLocation="+b.storeLocation:"")+(b.storePath?"&storePath="+b.storePath:"")+(b.storeAccess?"&storeAccess="+b.storeAccess:"");};var g=function(b,c,e){if(b.indexOf("&")>=0||b.indexOf("?")>=0)b=encodeURIComponent(b);return d+"?url="+b+"&key="+a.apikey+"&id="+e+"&referrer="+window.location.hostname+"&iframe="+(c.container!='window')+"&version="+a.API_VERSION+(c.services?"&s="+c.services.join(","):"")+(c.openTo!==undefined?"&loc="+c.openTo:"")+(c.mimetype!==undefined?"&m="+c.mimetype:"")+(c.extension!==undefined?"&ext="+c.extension:"")+(c.mobile!==undefined?"&mobile="+c.mobile:"")+(c.suggestedFilename!==undefined?"&defaultSaveasName="+c.suggestedFilename:"")+(c.signature?"&signature="+c.signature:"")+(c.policy?"&policy="+c.policy:"");};var h=function(b){return e+b.storage+"?key="+a.apikey+(b.base64decode?"&base64decode=true":"")+(b.mimetype?"&mimetype="+b.mimetype:"")+(b.filename?"&filename="+b.filename:"")+(b.path?"&path="+b.path:"")+(b.access?"&access="+b.access:"")+(b.signature?"&signature="+b.signature:"")+(b.policy?"&policy="+b.policy:"");};var i=function(a,b){return a+"?nonce=fp"+(!!b.base64decode?"&base64decode=true":"")+(b.mimetype?"&mimetype="+b.mimetype:"")+(b.signature?"&signature="+b.signature:"")+(b.policy?"&policy="+b.policy:"");};var j=function(){var b=a.util.parseUrl(window.location.href);return b.origin+"/404";};return{BASE:b,COMM:b+"/dialog/comm_iframe/",FP_COMM_FALLBACK:b+"/dialog/comm_hash_iframe/",STORE:e,PICK:c,EXPORT:d,constructPickUrl:f,constructExportUrl:g,constructWriteUrl:i,constructStoreUrl:h,constructHostCommFallback:j};});filepicker.extend("util",function(){var a=this;var b=function(a){return a&&Object.prototype.toString.call(a)==='[object Array]';};var c=function(a){return a&&Object.prototype.toString.call(a)==='[object File]';};var d=function(a){if(typeof HTMLElement==="object")return a instanceof HTMLElement;else return a&&typeof a==="object"&&a.nodeType===1&&typeof a.nodeName==="string";};var e=function(a){return d(a)&&a.tagName=="INPUT"&&a.type=="file";};var f=function(a){if(a===null)return 'null';else if(b(a))return 'array';else if(c(a))return 'file';return typeof a;};var g=function(){var a=new Date();return a.getTime().toString();};var h=function(a,b,c){if(a[b]===undefined)a[b]=c;};var i=function(a){if(window.jQuery)window.jQuery(function(){a();});else{var b="load";if(window.addEventListener)window.addEventListener(b,a,false);else if(window.attachEvent)window.attachEvent("on"+b,a);else if(window.onload){var c=window.onload;window.onload=function(){c();a();};}else window.onload=a;}};var j=function(a){return typeof a=="string"&&a.match("www.filepicker.io/api/file/");};var k=function(a){return function(){if(window.console&&typeof window.console[a]=="function")try{window.console[a].apply(window.console,arguments);}catch(b){alert(Array.prototype.join.call(arguments,","));}};};var l={};l.log=k("log");l.error=k("error");var m=function(a){var b={};for(key in a)b[key]=a[key];return b;};var n=function(a){var b={};b.url=a.url;b.filename=a.filename||a.name;b.mimetype=a.mimetype||a.type;b.size=a.size;b.key=a.key||a.s3_key;b.isWriteable=!!(a.isWriteable||a.writeable);return b;};return{isArray:b,isFile:c,isElement:d,isFileInputElement:e,getId:g,setDefault:h,typeOf:f,addOnLoad:i,isFPUrl:j,console:l,clone:m,standardizeFPFile:n};});filepicker.extend("widgets",function(){var a=this;var b=function(a,b,c,d){var e=d.getAttribute(c);if(e)b[a]=e;};var c=function(a,b){var c;if(document.createEvent){c=document.createEvent('Event');c.initEvent("change",true,false);c.fpfile=b?b[0]:undefined;c.fpfiles=b;a.dispatchEvent(c);}else if(document.createEventObject){c=document.createEventObject('Event');c.eventPhase=2;c.currentTarget=c.srcElement=c.target=a;c.fpfile=b?b[0]:undefined;c.fpfiles=b;a.fireEvent('onchange',c);}else if(a.onchange)a.onchange(b);};var d=function(d){var e=document.createElement("button");e.setAttribute('type','button');e.innerHTML=d.getAttribute('data-fp-button-text')||d.getAttribute('data-fp-text')||"Pick File";e.className=d.getAttribute('data-fp-button-class')||d.getAttribute('data-fp-class')||d.className;d.style.display="none";var f={};b("container",f,"data-fp-option-container",d);b("maxSize",f,"data-fp-option-maxsize",d);b("mimetype",f,"data-fp-mimetype",d);b("mimetypes",f,"data-fp-mimetypes",d);b("extension",f,"data-fp-extension",d);b("extensions",f,"data-fp-extensions",d);b("container",f,"data-fp-container",d);b("maxSize",f,"data-fp-maxSize",d);b("openTo",f,"data-fp-openTo",d);b("debug",f,"data-fp-debug",d);b("signature",f,"data-fp-signature",d);b("policy",f,"data-fp-policy",d);b("storeLocation",f,"data-fp-store-location",d);b("storePath",f,"data-fp-store-path",d);b("storeAccess",f,"data-fp-store-access",d);var g=d.getAttribute("data-fp-services");if(!g)g=d.getAttribute("data-fp-option-services");if(g){g=g.split(",");for(var h=0;h<g.length;h++)g[h]=a.services[g[h].replace(" ","")];f.services=g;}var i=d.getAttribute("data-fp-service");if(i)f.service=a.services[i.replace(" ","")];if(f.mimetypes)f.mimetypes=f.mimetypes.split(",");if(f.extensions)f.extensions=f.extensions.split(",");var j=d.getAttribute("data-fp-apikey");if(j)a.setKey(j);var k=(d.getAttribute("data-fp-multiple")||d.getAttribute("data-fp-option-multiple")||"false")=="true";if(k)e.onclick=function(){e.blur();a.pickMultiple(f,function(a){var b=[];for(var e=0;e<a.length;e++)b.push(a[e].url);d.value=b.join();c(d,a);});return false;};else e.onclick=function(){e.blur();a.pick(f,function(a){d.value=a.url;c(d,[a]);});return false;};d.parentNode.insertBefore(e,d);};var e=function(d){var e=document.createElement("div");e.className=d.getAttribute('data-fp-class')||d.className;e.style.padding="1px";d.style.display="none";d.parentNode.insertBefore(e,d);var i=document.createElement("button");i.setAttribute('type','button');i.innerHTML=d.getAttribute('data-fp-button-text')||"Pick File";i.className=d.getAttribute('data-fp-button-class')||'';e.appendChild(i);var j=document.createElement("div");g(j);j.innerHTML=d.getAttribute('data-fp-drag-text')||"Or drop files here";j.className=d.getAttribute('data-fp-drag-class')||'';e.appendChild(j);var k={};b("container",k,"data-fp-option-container",d);b("maxSize",k,"data-fp-option-maxsize",d);b("mimetype",k,"data-fp-mimetype",d);b("mimetypes",k,"data-fp-mimetypes",d);b("extension",k,"data-fp-extension",d);b("extensions",k,"data-fp-extensions",d);b("container",k,"data-fp-container",d);b("maxSize",k,"data-fp-maxSize",d);b("openTo",k,"data-fp-openTo",d);b("debug",k,"data-fp-debug",d);b("signature",k,"data-fp-signature",d);b("policy",k,"data-fp-policy",d);b("storeLocation",k,"data-fp-store-location",d);b("storePath",k,"data-fp-store-path",d);b("storeAccess",k,"data-fp-store-access",d);var l=d.getAttribute("data-fp-services");if(!l)l=d.getAttribute("data-fp-option-services");if(l){l=l.split(",");for(var m=0;m<l.length;m++)l[m]=a.services[l[m].replace(" ","")];k.services=l;}var n=d.getAttribute("data-fp-service");if(n)k.service=a.services[n.replace(" ","")];if(k.mimetypes)k.mimetypes=k.mimetypes.split(",");if(k.extensions)k.extensions=k.extensions.split(",");var o=d.getAttribute("data-fp-apikey");if(o)a.setKey(o);var p=(d.getAttribute("data-fp-multiple")||d.getAttribute("data-fp-option-multiple")||"false")=="true";if(a.dragdrop.enabled())h(j,p,k,d);else j.innerHTML="&nbsp;";if(p)j.onclick=i.onclick=function(){i.blur();a.pickMultiple(k,function(a){var b=[];var e=[];for(var g=0;g<a.length;g++){b.push(a[g].url);e.push(a[g].filename);}d.value=b.join();f(d,j,e.join(', '));c(d,a);});return false;};else j.onclick=i.onclick=function(){i.blur();a.pick(k,function(a){d.value=a.url;f(d,j,a.filename);c(d,[a]);});return false;};};var f=function(b,d,e){d.innerHTML=e;d.style.padding="2px 4px";d.style.cursor="default";d.style.width='';var f=document.createElement("span");f.innerHTML="X";f.style.borderRadius="8px";f.style.fontSize="14px";f.style.cssFloat="right";f.style.padding="0 3px";f.style.color="#600";f.style.cursor="pointer";var h=function(e){if(!e)e=window.event;e.cancelBubble=true;if(e.stopPropagation)e.stopPropagation();g(d);if(!a.dragdrop.enabled)d.innerHTML='&nbsp;';else d.innerHTML=b.getAttribute('data-fp-drag-text')||"Or drop files here";b.value='';c(b);return false;};if(f.addEventListener)f.addEventListener("click",h,false);else if(f.attachEvent)f.attachEvent("onclick",h);d.appendChild(f);};var g=function(a){a.style.border="1px dashed #AAA";a.style.display="inline-block";a.style.margin="0 0 0 4px";a.style.borderRadius="3px";a.style.backgroundColor="#F3F3F3";a.style.color="#333";a.style.fontSize="14px";a.style.lineHeight="22px";a.style.padding="2px 4px";a.style.verticalAlign="middle";a.style.cursor="pointer";a.style.overflow="hidden";};var h=function(b,d,e,g){var h=b.innerHTML;var j;a.dragdrop.makeDropPane(b,{multiple:d,maxSize:e.maxSize,mimetypes:e.mimetypes,mimetype:e.mimetype,extensions:e.extensions,extension:e.extension,location:e.storeLocation,path:e.storePath,access:e.storeAccess,policy:e.policy,signature:e.signature,dragEnter:function(){b.innerHTML="Drop to upload";b.style.backgroundColor="#E0E0E0";b.style.border="1px solid #000";},dragLeave:function(){b.innerHTML=h;b.style.backgroundColor="#F3F3F3";b.style.border="1px dashed #AAA";},onError:function(a,c){if(a=="TooManyFiles")b.innerHTML=c;else if(a=="WrongType")b.innerHTML=c;else if(a=="NoFilesFound")b.innerHTML=c;else if(a=="UploadError")b.innerHTML="Oops! Had trouble uploading.";},onStart:function(a){j=i(b);},onProgress:function(a){if(j)j.style.width=a+"%";},onSuccess:function(a){var d=[];var e=[];for(var h=0;h<a.length;h++){d.push(a[h].url);e.push(a[h].filename);}g.value=d.join();f(g,b,e.join(', '));c(g,a);}});};var i=function(a){var b=document.createElement("div");var c=a.offsetHeight-2;b.style.height=c+"px";b.style.backgroundColor="#0E90D2";b.style.width="2%";b.style.borderRadius="3px";a.style.width=a.offsetWidth+"px";a.style.padding="0";a.style.border="1px solid #AAA";a.style.backgroundColor="#F3F3F3";a.style.boxShadow="inset 0 1px 2px rgba(0, 0, 0, 0.1)";a.innerHTML="";a.appendChild(b);return b;};var j=function(c){c.onclick=function(){var d=c.getAttribute("data-fp-url");if(!d)return true;var e={};b("container",e,"data-fp-option-container",c);b("suggestedFilename",e,"data-fp-option-defaultSaveasName",c);b("container",e,"data-fp-container",c);b("suggestedFilename",e,"data-fp-suggestedFilename",c);b("mimetype",e,"data-fp-mimetype",c);b("extension",e,"data-fp-extension",c);var f=c.getAttribute("data-fp-services");if(!f)f=c.getAttribute("data-fp-option-services");if(f){f=f.split(",");for(var g=0;g<f.length;g++)f[g]=a.services[f[g].replace(" ","")];e.services=f;}var h=c.getAttribute("data-fp-service");if(h)e.service=a.services[h.replace(" ","")];apikey=c.getAttribute("data-fp-apikey");if(apikey)a.setKey(apikey);a.exportFile(d,e);return false;};};var k=function(){if(document.querySelectorAll){var a;var b=document.querySelectorAll('input[type="filepicker"]');for(a=0;a<b.length;a++)d(b[a]);var c=document.querySelectorAll('input[type="filepicker-dragdrop"]');for(a=0;a<c.length;a++)e(c[a]);var f=[];var g=document.querySelectorAll('button[data-fp-url]');for(a=0;a<g.length;a++)f.push(g[a]);g=document.querySelectorAll('a[data-fp-url]');for(a=0;a<g.length;a++)f.push(g[a]);g=document.querySelectorAll('input[type="button"][data-fp-url]');for(a=0;a<g.length;a++)f.push(g[a]);for(a=0;a<f.length;a++)j(f[a]);}};var l=function(a){if(a.jquery)a=a[0];var b=a.getAttribute('type');if(b=='filepicker')d(a);else if(b=='filepicker-dragdrop')e(a);else j(a);};return{constructPickWidget:d,constructDragWidget:e,constructExportWidget:j,buildWidgets:k,constructWidget:l};});filepicker.extend('window',function(){var a=this;var b={OPEN:'/dialog/open/',SAVEAS:'/dialog/save/'};var c="filepicker_dialog";var d="left=100,top=100,height=600,width=800,menubar=no,toolbar=no,location=no,personalbar=no,status=no,resizable=yes,scrollbars=yes,dependent=yes,dialog=yes";var e=1000;var f=function(){if(document.body&&document.body.offsetWidth){winW=document.body.offsetWidth;winH=document.body.offsetHeight;}if(document.compatMode=='CSS1Compat'&&document.documentElement&&document.documentElement.offsetWidth){winW=document.documentElement.offsetWidth;winH=document.documentElement.offsetHeight;}if(window.innerWidth&&window.innerHeight){winW=window.innerWidth;winH=window.innerHeight;}return [winW,winH];};var g=function(){var b=f()[0]<768;var c=a.cookies.THIRD_PARTY_COOKIES===false;return a.browser.isIOS()||a.browser.isAndroid()||b||c;};var h=function(b,f,h){h=h||function(){};if(!b)b='modal';if(b=='modal'&&g())b='window';if(b=='window'){var i=c+a.util.getId();var j=window.open(f,i,d);var k=window.setInterval(function(){if(!j||j.closed){window.clearInterval(k);h();}},e);}else if(b=='modal')a.modal.generate(f,h);else{var l=document.getElementById(b);if(!l)throw new a.FilepickerException("Container '"+b+"' not found. This should either be set to 'window','modal', or the ID of an iframe that is currently in the document.");l.src=f;}};return{open:h,WINDOW_NAME:c,getSize:f,shouldForce:g};});(function(){filepicker.internal(function(){var a=this;a.util.addOnLoad(a.cookies.checkThirdParty);a.util.addOnLoad(a.widgets.buildWidgets);});delete filepicker.internal;delete filepicker.extend;var a=filepicker._queue||[];var b;var c=a.length;if(c)for(var d=0;d<c;d++){b=a[d];filepicker[b[0]].apply(filepicker,b[1]);}delete filepicker._queue;})();
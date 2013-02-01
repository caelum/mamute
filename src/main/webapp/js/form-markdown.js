$(function() {
	if($(".wmd-panel").length > 0) {
		var converter1 = Markdown.getSanitizingConverter();
		var editor1 = new Markdown.Editor(converter1);
		editor1.run();
	}
	$(".wmd-display").each(function(i, el) {
		var display = $(el);
		var divs = display.children("div");
		var from = $(divs[0]);
		var target = $(divs[1]);
		var converter = new Markdown.Converter();
	    target.html(converter.makeHtml(from.html()));
	});
});
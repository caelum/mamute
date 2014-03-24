manipulateBanner();

function manipulateBanner(){
	var banner = $(".about-banner");
	if(localStorage.minimizedBanner == "true") toggleBanner(banner);
}

$(".minimize-banner").click(function(){
	var banner = $(this).closest(".about-banner");
	toggleBanner(banner);
	localStorage.minimizedBanner = $(".minimized-banner")[0] != undefined;
});

function toggleBanner(banner){
	banner.find(".how-it-works").toggle(300);
	banner.find(".tell-me-more").toggleClass("minimized-banner");
}$(function() {
	$(".answer-form textarea").focus(function(e) {
		var form = $(this).parents(".answer-form");
		if (form.data("same-author") && !form.data("warned")) {
			var confirm = $(".same-author-confirmation");
			var textarea = form.find("textarea");
			var submitButton = form.find("input[type='submit']");
			var markdownBar = $("#wmd-button-bar");
			var inputs = form.find("input");
			
			textarea.parent().append(confirm);
			confirm.show();
			
			submitButton.addClass("opaque");
			markdownBar.addClass("opaque");
			inputs.attr("disabled", true);
			confirm.find("button").click(function() {
				confirm.hide();
				textarea.show();
				markdownBar.removeClass("opaque");
				submitButton.removeClass("opaque");
				inputs.attr("disabled", false);
				return false;
			});
			textarea.hide();
			form.data("warned", true);
		}
	});
});
	
$(".approve-news").click(function(e){
	e.preventDefault();
	var self = $(this);
	$.post(self.attr("href"), function(){
		self.closest(".post-item").removeClass("post-under-review");
		self.remove();		
	});
});$(function(){
	$(".ban-user").on('click', toogleBan);
	
	function toogleBan(event){
		event.preventDefault();
		var post = $(this);
		
		$.ajax({
			url: post.attr("data-url"),
			method: "POST",
			beforeSend: function() {
				changeText(post.html());
			},
			error: function(jqXHR) {
				errorPopup("Ocorreu um erro...", post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function changeText(text){
			text == 'Bloquear usuário' ? post.html('Desbloquear usuário') : post.html('Bloquear usuário');
		}
	}
	
});$(".button-hint").click(function(event){
	var self = $(this),
		target = $("#"+self.data("button-hint-id"));
	$(".button-hint-text").not(target).hide(100);
	$(".button-hint").not(self).removeClass("active");
	target.toggle(150);
	self.toggleClass("active");
	
});
$(".history-form").each(function(i, item){
	$(".history-comparison").prettyTextDiff({
		originalContainer:".history-current .post-text",
		changedContainer: $(item).find(".history-version .post-text").first(),
		diffContainer:$(item).find(".history-diff.post-text").first(),
		cleanup:true
	});
});
$(function(){
	$(".dropdown-trigger").click(function(event){
		event.preventDefault();
		$("#"+$(this).data("target-id")).toggle();
	});
});$(".flag-it").click(function(e) {
	e.preventDefault();
	var link = $(this);
	var commentOptions = link.parent();
	var comment = link.parents('.comment');
	var modal = new Modal($("#" + link.data("modal-id")));
	var form = modal.element.find("form");
	var uri = form.attr("action");

	link.toggleClass('selected');
	comment.toggleClass('to-flag');

	var callbacks = {};
	callbacks["409"] = function() {
		errorPopup("Você não pode fazer isso.", modal.element, "center-popup");
	};
	callbacks["400"] = function() {
		errorPopup("Escolha uma opção.", modal.element, "center-popup");
	};
	callbacks["403"] = function() {
		errorPopup("Você deve estar logado.", modal.element, "center-popup");
	};
	callbacks["200"] = function() {
		modal.hide(200);
		link.remove();
	};
	
	var errors = form.find(".error");
	
	form.change(function(){
		errors.text("");
	});

	form.submit(function(e) {
		e.preventDefault();
		var checked = form.find("input:radio:checked");
		if (isEmpty(checked)) {
			errors.text("Escolha um motivo").show();
			return;
		}
		reason = form.find("textarea");
		if (checked.val() == "OTHER" && isEmpty(reason.val())) {
			errors.text("Descreva o motivo").show();
			return;
		}
		$.ajax(uri, {
			complete : function(xhr, textStatus) {
				if (callbacks[xhr.status] != undefined) {
					callbacks[xhr.status].call();
				} else {
					errorPopup("Ocorreu um erro.", modal, "center-popup");
					console.log(xhr);
				}
			},
			data : form.serialize(),
			headers : {
				Accept : "application/json"
			},
			method : "POST"
		});
	});

});

$(".other-option").change(function() {
	var self = $(this);
	self.siblings("#other-reason").show(200);
});

$(".modal input:not(.other-option)").change(function() {
	$(".modal #other-reason").hide();
});

function isEmpty(el) {
	return el.length == 0;
}$("form").not(".ajax").submit(function(e) {
	var form = $(this)
	if (form.valid()) {
		var input = form.find("input[type=submit]");
		input.attr("disabled", "true");
		form.addClass("inactive");
	}
});
$(function(){
	$(".hintable").each(function(index, hintable){
		var currentHintable = $(hintable),
			anchor = currentHintable.data("hint-id"),
			label = $("label[for="+currentHintable.attr("id")+"]"),
			hintAnchor = $("<a class='hint-anchor' href='#"+anchor+"'> ?</a>");
		hintAnchor.appendTo(label);
	});
	
	var toggleHintFor = function(element) {
		var hint = $("#" + $(element).data("hint-id"));
		if(!$(hint).is(":visible")){
			$(".hint").hide();
			hint.fadeIn(500);
		}
	};
	
	$(".hintable").focus(function() {
		toggleHintFor(this);
	});
	
	$(".hintable").focusout(function() {
		var hint = $(".answer-form > .hint");
		hint.fadeOut(500);
	});
	
	$('#question-title').focus();
});$(function() {
	marked.setOptions({
	  sanitize: true,
	});
	$(".wmd-input").on('keyup', function(){
		var self = $(this),
		preview = self.closest(".wmd").find(".md-preview");
		if(self.val().length == 0){
			preview.addClass("hidden");
		}else{
			preview.removeClass("hidden");
			preview.html(marked(self.val()));
		}
	});

	if($(".wmd-panel").length > 0) {
		var converter1 = Markdown.getSanitizingConverter();
		var editor1 = new Markdown.Editor(converter1);
		editor1.run();
	}
	
});$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    email: "Este email não é válido",
	    equalTo: jQuery.validator.format("Os valores inseridos são diferentes."),
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	    maxlength: jQuery.validator.format("Por favor, insira no máximo {0} caracteres."),
	});
	
	$.validator.addMethod(
	    "date",
	    function(value, element) {
	    	if(value.length == 0){
	    		return true;	
	    	}
	        return value.match(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/(19[1-9][0-9]|20[01][0-3])$/);
	    },
	    "Esta data não é válida. Utilize uma data no formato dd/mm/yyyy"
	);
	

	$.validator.addMethod(
			"brutal-url",
			function(value, element) {
				if(value.length == 0){
					return true
				}
				return value.match(/(www\.)?(.*\.)(.*)/);
			},
			"Insira uma url válida!"
	);
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
});
$(".hide-next").click(function(){
	var hideButton = $(this)
    var toHide = $(hideButton.next());
    if (toHide.css("display") == "block"){
    	toHide.css("display", "none");
    	hideButton.children().attr("class", "icon-angle-right")
    }else{
    	toHide.css("display", "block");
    	hideButton.children().attr("class", "icon-angle-down")
    }
});

$(window).resize(function(){
	var hideButton = $(".hide-next");
	var toHide = $(hideButton.next());
	if ($(window).width() >= 501){	
		if (toHide.css("display") == "none" ){
			toHide.css("display", "inline-block");
		}
	}else{
		toHide.css("display", "none");
		hideButton.children().attr("class", "icon-angle-right");
	}
});$(".history-select-version").change(function(event) {
	var selected = $(this).val();
	$(".history-forms-area").addClass("hidden").eq(selected).removeClass("hidden");
});

$(".toggle-version").click(function(){
	$(this).siblings('.history-version').toggleClass("hidden");
	$(this).siblings('.history-diff').toggleClass("hidden");
	$(this).siblings(".toggle-version").toggleClass("hidden");
	$(this).toggleClass("hidden");
});$(function() {
	$('.requires-karma').each(function(index, element) {
		var element = $(element);
		var authorCan = !element.hasClass("author-cant");
		var required = parseInt(element.data("karma"));
		var isAuthor = element.data("author");
		isAuthor = isAuthor == undefined ? false : isAuthor;
		if (authorCan && isAuthor) {
			return;
		}
		if(!MODERATOR && required > KARMA) {
			element.off('click');
			element.click(function(e) {
				e.preventDefault();
				errorPopup("Você precisa ter "+ required +" pontos de reputação!", this);
			});
		}
	});
	$('.author-cant').each(function(index, element) {
		var element = $(element);
		var isAuthor = element.data("author");
		if(isAuthor) {
			element.off('click');
			element.click(function(e) {
				e.preventDefault();
				errorPopup("O autor não pode realizar esta operação!", this);
			});
		}
	});
	if(!LOGGED_IN) {
		$('.requires-login').off('click');
		$('.requires-login').bind("click", function(e) {
			e.preventDefault();
			errorPopup("Você precisa estar logado!", this);
		});
	}
	$('.iframe-load').click(function() {
		var link = $(this);
		var iframe = $("<iframe/>");
		iframe.attr("src", link.attr("href"));
		link.parent().html(iframe);
	});
	
});$(function(){
	$(".mark-as-solution").on('click', markAsSolution);
	
	function markAsSolution(event){
		event.preventDefault();
		var mark = $(this);
		var markParent = $(mark).parent();
		var oldSolutionParent = $(".solution-container");
		var oldSolution = $(oldSolutionParent).find(".mark-as-solution");
		
		$.ajax({
			url:mark.attr("href"),
			method: "POST",
			beforeSend: function() {
				toggleAll();
			},
			error: function(jqXHR) {
				toggleAll();
				errorPopup("Ocorreu um erro...", mark, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleAll(){
			if(notOldSolution()) toggleClassesOf(oldSolutionParent);
			toggleClassesOf(markParent);
		}
		
		function notOldSolution(){
			return mark[0] != oldSolution[0];
		}
		
		function toggleClassesOf(parent){
			parent.toggleClass("solution-container").toggleClass("not-solution-container");
		}
	};
	
});function Modal(modalElement){
	this.element = $(modalElement);
	this.closeButton = $(modalElement).find(".modal-close-button");
	this.toggle();
	this.closeButton.off("click");
	this.closeButton.on("click", closeModal);
	var self = this;
	function closeModal(e){
		e.preventDefault();
		self.toggle();
	}
}
Modal.prototype.toggle = function(){
	this.element.toggleClass('hidden');
}
Modal.prototype.hide = function(){
	this.element.hide();
}
$('.more-comments').click(function(){
	var collapsed = $(this).siblings(".comment-list").find(".collapsed");
	collapsed.toggleClass("hidden");
	
	if(!collapsed.hasClass("hidden")) $(this).html("Ocultar comentários");
	else $(this).html("Mostrar todos os <strong>" + $(this).attr("size") + "</strong> comentários");
});$(function(){
	var ANSWER = "respostas",
		QUESTION = "perguntas"
		WATCHED = "acompanhadas";
	
	$(".advanced-data-section").on("click", ".order-by" , function(event){
		event.preventDefault();
		var self = $(this);
		var href = self.attr("href");
		var target = $("#" + self.data("target-id")).parent();
		var subheader = target.find(".subheader");
		selectMenu(self);
		var subheaderHTML = subheader[0].outerHTML;
		$.get(href, function(list){
			target.html(subheaderHTML + list);
			changePagerUrl(target, href);
		});
	});

	$(".advanced-data-section").on("click", ".pager a", function(event){
		event.preventDefault();
		var self = $(this);
		var target = $("#" + self.data("target-id")).parent();
		var subheader = target.find(".subheader")[0].outerHTML;
		$.get(self.attr("href"), function(list) {
			target.html(subheader + list);
			selectPage(self.parent());
		});
	});
	
	function selectMenu(selectedMenu){
		$(selectedMenu).closest(".nav").find(".order-by").removeClass("selected");
		$(selectedMenu).addClass("selected");
	}
	
	function selectPage(pageSelected) {
		$(pageSelected).closest(".pager").find("li").removeClass("current");
		$(pageSelected).addClass("current");
	}
	
	function repopulateWith(target, list, type) {
		var listElements = "";
		$(list).each(function(index, item){
			var question = getQuestion(type, item),
				href = getHref(type, question, item);
			listElements += "<li class='ellipsis advanced-data-line'><span class='counter'>"+item.voteCount+"</span> <a href='"+href+"'>"+question.information.title+"</a></li>";
		});
		$(target).html(listElements);
	}
	
	function getHref(type, question, item){
		var answerAnchor;
		console.log(question);
		if(type == ANSWER){
			answerAnchor = "#answer-"+item.id;
		}
		return "/"+question.id+"-"+question.information.sluggedTitle+answerAnchor;
	}
	
	function getQuestion(type, item){
		if(type == QUESTION || type == WATCHED){
			return item;
		}else if(type == ANSWER){
			return item.question;
		}
	}
	
	function changePagerUrl(self, url){
		var pager = $(self).closest(".user-questions").find(".pager");
		pager.find("a").each(function(i,item){
			var parent = $(item).parent();
			parent.removeClass("current");
			if(i == 0) parent.addClass("current");  
			$(item).attr("href", url + "&p=" + ($(item).html()));
		});
	}
});$(function(){
	$("body").on('click', '.show-popup', function(event){
		event.preventDefault();
		$(this).parent().find(".popup").toggle();
	});
	
	$("body").on('click', '.close-popup', function(event){
		event.preventDefault();
		var self = $(this)
		if(self.hasClass("popup")){
			self.remove();
		}else{
			self.closest(".popup").remove();
		}
	});
});

$(function() {
	var warnings = $("<div>");
	warnings
		.addClass("question-warnings")
		.html("<h4 class='section-title hint-title'>Reveja o conteúdo da sua postagem:</h4><ul></ul>")
		.hide();
	
	$(".validated-form").eq(0).before(warnings);
	var messages = warnings.find("ul");
	
	function validateRule(isValid, message, messageClass) {
		if (isValid) {
			messages.append("<li class='hint-tip "+messageClass+"'>" + message + "</li>");
		}
	}
	
	function cleanMessages() {
		if (messages.find("li").length != 0) {
			warnings.show(100);
		} else {
			warnings.hide(100);
		}
	}
	
	function containsIllegalWords(text, illegalWords) {
		for (var i in illegalWords) {
			var word = illegalWords[i];
			if (text.toLowerCase().indexOf(word.toLowerCase()) != -1) {
				return true;
			}
		}
		return false;
	}
	
	$("#question-title").keyup(function() {
		var input = $(this);
		var title = input.val();
		var messageClass = "title-messages";
		messages.find("li."+messageClass).remove();
		var illegalWords = ["ajuda", "ajudar", "help", "socorro", "urgente", "please"];
		
		validateRule(title.toUpperCase() == title && title.length > 1, 
				"Não utilize apenas letras maiúsculas no título", 
				messageClass);
		validateRule(containsIllegalWords(title, illegalWords), 
				"Evite usar termos como 'Socorro', 'Urgente', 'Please' no título da sua pergunta", 
				messageClass);
		validateRule(containsIllegalWords(title, ["resolvido"]), 
				"Não coloque a palavra 'resolvido' no título, marque a resposta que resolveu sua pergunta como certa", 
				messageClass);
		
		cleanMessages();
	});
	
	$(".description-input").keyup(function() {
		var input = $(this);
		var description = input.val();
		var messageClass = "description-messages";
		messages.find("li."+messageClass).remove();
		var illegalWords = ["kkk", "vc", "!!", "??", "..."];
		validateRule(description.toUpperCase() == description && description.length > 1, 
				"Não utilize apenas letras maiúsculas na descrição da postagem", 
				messageClass);
		validateRule(containsIllegalWords(description, illegalWords), 
				"Deixe sua postagem clara e completa, evite informalidades (como 'vc', 'kkk') ou excesso de pontuação (como !!, ?? e ...)", 
				messageClass);
		cleanMessages();
	});
});$(function() {
	$(".share-button").click(function() {
		var url = $(this).data("shareurl");
		window.open(url, "share", "width=650, height=450")
	});
});$(function() {
	bindAll();
	
	function bindAll(){
		bind($(".simple-ajax-form a"), "click", showForm);
		bind($(".simple-ajax-form .cancel"), "click", hideForm);
		bind($('form.ajax'), "submit", submitForm);
	}

	function bind(element, event ,callback){
		element.off(event, callback);
		element.on(event, callback);
	}
	
	function showForm(e) {
		e.preventDefault();
		var formArea = $(this).siblings(".ajax-form");
		formArea.toggleClass("hidden");
		formArea.find(".to-focus").focus();
	}
	
	function hideForm(e){
		e.preventDefault();
		var form= $(this).closest("form.ajax");
		resetForm(form)
	}
	
	function submitForm(e) {
		e.preventDefault();
		executeAjax($(this));
	}
	
	function resetForm(form){
		var formParent = form.parent();
		form.removeClass("inactive");
		form.find("input[type='submit']").attr("disabled", false);
		formParent.addClass("hidden");
	}
	
	function executeAjax(form){
		if (!form.valid()) 
			return;

		form.find(".submit").attr("disabled", true);
		form.find(".cancel").attr("disabled", false);
		var error = function(jqXHR) {
			resetForm(form);
			if (jqXHR.status == 400) {
				errorPopup("Ocorreu um erro de validação inesperado.", form.parent(), "center-popup");
				return;
			}
			errorPopup("Ocorreu um erro.", form.parent(), "center-popup");
			console.log(jqXHR);
		};
	
		var success = function(response, status, jqhr) {
			var target = $("#" + form.data("ajax-result"));
			if (jqhr.status == 201) {
				target.append("<span class='suggestion-accepted'>Sugest&atilde;o enviada!</span>");
			} else {
				var action = form.data("ajax-on-callback") || "replace-inner";
				if (action == "replace-inner") {
					target.html(response);
				} else if(action == "append") {
					target.append(response);
				} else if(action == "replace"){
					target.replaceWith(response);
				}
				target.removeClass("hidden");
			}
			resetForm(form);
			bindAll();
		};
		
		var uri = form.attr("action");
		$.ajax(uri, {
			success: success,
			error: error,
			dataType : 'html',
			data : form.serialize(),
			method: "POST"
		});
	}
});
	
$(function(){
	var url = window.location.href;

	$($('.tabs').find('a').get().reverse()).each(function(i, item){
		if(url.indexOf(item) >= 0) {
			$(item).addClass("selected");
			return false;
		}
	});
});
var autoCompleteId;
var TagsManager = function(components){
	
	$("*:not(.autocomplete)").click(function(){
		$(".autocompleted-tags").addClass("hidden");
	});

	$.getJSON("/pergunta/allTags", function(json){
		var allTags = json;

		var element = $('.autocomplete');
		for ( var i in components) {
			var component = components[i];
			component.forElement(element, allTags);
		}
	});
}

var AutoCompleteDOM = function(){
	var splitChar = TAGS_SPLITTER_CHAR;
	
	var showAutoCompleteArea = function(target){
		target.removeClass("hidden");
		setLoading(target);
	}

	var suggestAutoComplete = function(target, tagChunk, input, allTags){
		if(tagChunk == undefined || tagChunk == splitChar || !tagChunk) return;
		suggestions = getSuggestions(tagChunk, allTags);
		showSuggestions(suggestions, target);
	}
	
	function getSuggestions(tagChunk, allTags){
		var regex = new RegExp(".*"+tagChunk+".*");
		var suggestions = $(allTags).map(function(index, tag){
			if(tag.name.match(regex)){
				return tag;
			}
		});
			
		return sortAndTrim(suggestions, tagChunk);
	}
	
	function sortAndTrim(array, tagChunk){
		return array.sort(function(tagA, tagB){
			return  tagA.name.indexOf(tagChunk) - tagB.name.indexOf(tagChunk);
		}).slice(0, 9);
	}
	
	var showSuggestions = function(suggestions, target){
		if(suggestions.length != 0){
			var suggestionElements = "";
			$(suggestions).each(function(index, suggestion){
				suggestionElements += "<li class='complete-tag'><a class='tag-brutal'>"+suggestion.name+"</a> x "+ suggestion.usageCount;
				suggestionElements += "<div class='tag-description'>"+suggestion.description+"</div></li>";
			});
			$(target).html(suggestionElements).removeClass("hidden");
			
			$('.autocompleted-tags .complete-tag').click(function(){
				var self = $(this);
				insertTagIntoTextArea(self.find(".tag-brutal").text());
			});
		}else{
			target.addClass("hidden");
		}
	}

	var insertTagIntoTextArea = function(text) {
		var input = $('input[name=tagNames]');
	    var inputValue = input.val();
	    var vetValue = inputValue.split(splitChar);
	    vetValue = vetValue.slice(0, vetValue.length - 1); // remove the last tag(that is incomplete)
	    vetValue.push(text);
	    input.val(vetValue.join(splitChar)+splitChar);
	    $(input).valid();
	    input.focus();
	    $(".autocompleted-tags").html("");
	}
	
	function isNotAControl(key){
		keyboardCtrlAutoCompleteBox = [13, 27, 37, 38, 39, 40];
		return $.inArray(key, keyboardCtrlAutoCompleteBox) < 0
	}
	
	function escapeSpecialCharacter(tagChunk){
	   specialCharacters = ['+','.','|','$','*','^','(',')'];
	   pattern = specialCharacters.join("");
	   tagChunk = tagChunk.replace(new RegExp("([" + pattern + "])", 'g'), "\\$1");
	   return tagChunk;
	}
	
	return {
		forElement: function(element, allTags){
			element.keyup(function(e){
				var autoCompleteInput = $(this),
				target = $("#"+autoCompleteInput.data("autocomplete-id")),
				tagChunk = $(autoCompleteInput.val().split(splitChar)).last().get(0);
				
				if(!tagChunk){
					target.addClass("hidden");
					return;
				}
				
				
				if(isNotAControl(e.which)) { 
					showAutoCompleteArea(target);
					
					clearTimeout(autoCompleteId);
					
					autoCompleteId = setTimeout(function(){
						suggestAutoComplete(target, escapeSpecialCharacter(tagChunk), autoCompleteInput, allTags)
					},100);
				}

			});
		}
	}
}

var TagsNavigation = function(){
	return {
		forElement: function(element){
			pos = -3;
			element.keydown(function(e){
				arrow = {left: 37, up: 38, right: 39, down: 40};
				control = {esc: 27, enter: 13};
				var completeTag = $('.complete-tag');

				switch(e.which) {
					case arrow.down:
						if(pos < completeTag.length - 3) pos += 3;
						break;
						
					case arrow.up:
						if(pos > 0) pos -= 3;
						break;
						
					case arrow.right:
						if(pos < completeTag.length - 1) pos++;
						break;
						
					case arrow.left:
						if(pos > 0)	pos--;
						break;
						
					case control.enter:
						e.preventDefault();
						completeTag.eq(pos).click();
						pos=-3;
						$('.autocompleted-tags').addClass('hidden');
						
					case control.esc:
						$('.autocompleted-tags').addClass('hidden');
						break;
						
					default: return;
				}

				completeTag.removeClass('tag-selected');
				completeTag.eq(pos).addClass('tag-selected');
			});		
		}
		
	}
	
}

var TagsValidator = function(){
	var splitChar = TAGS_SPLITTER_CHAR;
	return {
		forElement : function(element, allTags){
			var allTagNames = $.makeArray(
				$(allTags).map(function(index, element){
					return element.name;
				})
			);
			
			var tagsNotFound = [];
			$.validator.addMethod(
					"only-existent-tags",
					function(value, element) {
						var tags = $($(element).val().split(splitChar));
						tagsNotFound = verifyIfExists(tags, tagsNotFound);
						var valid = tagsNotFound.length <= 0;
						return valid;
					},
					function(){
						return "Use apenas tags que existem! As seguintes tags não existem: <b>"+ tagsNotFound + "</b>";
					}
			);
			
			function verifyIfExists(tags, tagsNotFound){
				tagsNotFound = [];
				tags.each(function(index, tag){
					if(tag != "" && notContains(allTagNames, tag) && notContains(tagsNotFound, tag)){
						tagsNotFound.push(tag);
					}
			});
				return tagsNotFound;
			}
			
			function notContains(array, item){
				return array.indexOf(item) < 0;
			}
			
		}
		
	}
		
}

(function() {
	var components = [new AutoCompleteDOM(), new TagsNavigation()];
	if (ANYONE_CAN_CREATE_TAGS != "true") {
		components.push(new TagsValidator());
	}
	new TagsManager(components);
	
}());


$(".vote-option").bind("click", function() {
	if(!$(this).hasClass("stop")) {
		if (!$(this).hasClass("voted")) {
			$(this).addClass("stop");
			vote($(this));
		}
		else if ($(this).hasClass("voted")) {
			$(this).addClass("stop");
			removeVote($(this));
		}
	}
	
});

function vote(link) {
	var vote = link.data("value");
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/voto/"+ vote;

	fakeUpdateIncrement(link, vote);
	
	$.ajax(""+ params, {
		complete: function(jqXHR, textStatus) {
			if (jqXHR.status == "200") {
				var count = jqXHR.responseText;
				voteSuccess(link, count);
			} else if (jqXHR.status == "409") {
				errorPopup("Você não pode votar na própria questão", link);
			} else if (jqXHR.status == "403") {
				errorPopup(jqXHR.responseText, link);
			} else {
				errorPopup("Ocorreu um erro", link);
				console.log(jqXHR);
			}
			$(".vote-option").removeClass("stop");
		},
		accepts: "application/json",
		method: "POST"
	});
	
}

function removeVote(link) {
	var vote = link.data("value");
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/voto/remove/"+ vote;
	
	fakeUpdateDecrement(link, vote);
	
	$.ajax(""+ params, {
		complete: function(jqXHR, textStatus) {
			if (jqXHR.status == "200") {
				var count = jqXHR.responseText;
				voteRemovalSuccess(link, count);
			} else if (jqXHR.status == "409") {
				errorPopup("Você não pode votar na própria questão", link);
			} else if (jqXHR.status == "403") {
				errorPopup(jqXHR.responseText, link);
			} else {
				errorPopup("Ocorreu um erro", link);
				console.log(jqXHR);
			}
			$(".vote-option").removeClass("stop");
		},
		accepts: "application/json",
		method: "POST"
	});
}

function highlight(link) {
	link.addClass("voted").siblings().removeClass("voted");
}

function removeHighlight(link) {
	link.removeClass("voted").siblings().removeClass("voted");
}

function updateCount(link, count) {
	var voteCount = $(link).closest(".vote-container").find(".vote-count");
	voteCount.text(count);
}

function fakeUpdateIncrement(link, vote) {
	var value;
	var countWithoutAjax = parseInt($(link).closest(".vote-container").find(".vote-count").html());
	
	if(vote == "positivo") value = countWithoutAjax + 1;
	else value = countWithoutAjax - 1;
	
	voteSuccess(link, value);
}

function fakeUpdateDecrement(link, vote) {
	var value;
	var countWithoutAjax = parseInt($(link).closest(".vote-container").find(".vote-count").html());
	
	if(vote == "positivo") value = countWithoutAjax - 1;
	else value = countWithoutAjax + 1;
	
	voteRemovalSuccess(link, value);
}


function voteSuccess(link, count) {
	highlight(link);
	updateCount(link, count);
}

function voteRemovalSuccess(link, count) {
	removeHighlight(link);
	updateCount(link, count);
}

$(function(){
	$(".watch").on('click', watch);
	
	function watch(event){
		event.preventDefault();
		var post = $(this);
		var icon = post.find("span")
		
		$.ajax({
			url:post.attr("href"),
			method: "POST",
			beforeSend: function() {
				toggleClassOf(icon);
			},
			error: function(jqXHR) {
				errorPopup("Ocorreu um erro...", post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleClassOf(icon){
			icon.toggleClass("icon-muted");
			icon.toggleClass("icon-eye-off");
			icon.toggleClass("icon-eye");
			
			if(icon.hasClass("icon-muted")) {
				icon.attr("title", "Clique aqui para ser notificado de novidades nessa questão e suas respostas.");
			} else {
				icon.attr("title", "Clique aqui para cancelar a notificação de novidades nessa questão e suas respostas.");
			}
		}
	}
});
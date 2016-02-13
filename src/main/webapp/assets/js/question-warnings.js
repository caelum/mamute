

$(function() {
	var warnings = $("<div>");
	warnings
		.addClass("question-warnings")
		.html("<h4 class='section-title hint-title'>"+Messages.get('validation.bad_post')+"</h4><ul></ul>")
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
		var illegalWords = [Messages.get('metas.help'), Messages.get('metas.help_me'), 
		                    "help", Messages.get('metas.assist_me'), Messages.get('metas.urgent'), "please"];
		
		validateRule(title.toUpperCase() == title && title.length > 1, 
				Messages.get('validation.avoid_only_uppercase_title'), 
				messageClass);
		validateRule(containsIllegalWords(title, illegalWords), 
				Messages.get('validation.avoid_use_of'), 
				messageClass);
		validateRule(containsIllegalWords(title, ["resolvido"]), 
				Messages.get('validation.do_not_use'), 
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
				Messages.get('validation.avoid_only_uppercase_title'), 
				messageClass);
		validateRule(containsIllegalWords(description, illegalWords), 
				Messages.get('validation.make_clear_post'), 
				messageClass);
		cleanMessages();
	});
});
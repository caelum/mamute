

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
		var illegalWords = [MESSAGES['help'], MESSAGES['help_me'], "help", MESSAGES['assist_me'], MESSAGES['urgent'], "please"];
		
		validateRule(title.toUpperCase() == title && title.length > 1, 
				MESSAGES['avoid_only_uppercase_title'], 
				messageClass);
		validateRule(containsIllegalWords(title, illegalWords), 
				MESSAGES['avoid_use_of'], 
				messageClass);
		validateRule(containsIllegalWords(title, ["resolvido"]), 
				MESSAGES['do_not_use'], 
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
				MESSAGES['avoid_only_uppercase_post'], 
				messageClass);
		validateRule(containsIllegalWords(description, illegalWords), 
				MESSAGES['make_clear_post'], 
				messageClass);
		cleanMessages();
	});
});
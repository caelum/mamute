

$(function() {
	var warnings = $("<div>");
	warnings
		.addClass("question-warnings")
		.html("<h4 class='section-title hint-title'>Reveja o conteúdo da sua postagem:</h4><ul></ul>")
		.hide();
	
	$(".md-panel.md-preview").before(warnings);
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
});
$(function() {
	$('.requires-karma').each(function(index, element) {
		var element = $(element);
		console.log(element);
		var authorCan = !element.hasClass("author-cant");
		var required = parseInt(element.data("karma"));
		var isAuthor = element.data("author");
		isAuthor = isAuthor == undefined ? false : isAuthor;
		if (authorCan && isAuthor) {
			return;
		}
		console.log(required);
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
	
});
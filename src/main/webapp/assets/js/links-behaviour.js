$(function() {
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
				errorPopup(Messages.get('need.to_have') + " " + required + " " + Messages.get('reputation_score') + "!", this);
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
				errorPopup(Messages.get('error.author.cant_realize_operation'), this);
			});
		}
	});
	if(!LOGGED_IN) {
		$('.requires-login').off('click');
		$('.requires-login').bind("click", function(e) {
			e.preventDefault();
			errorPopup(Messages.get('auth.requires_login'), this);
		});
	}
	$('.iframe-load').click(function() {
		var link = $(this);
		var iframe = $("<iframe/>");
		iframe.attr("src", link.attr("href"));
		link.parent().html(iframe);
	});
	
});
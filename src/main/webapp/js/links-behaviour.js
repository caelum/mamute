$(function() {
	$('.requires-karma').each(function(i, el) {
		var x = $(el);
		var required = parseInt(x.data("karma"));
		if(!MODERATOR && required > KARMA && x.data("author")=="false") {
			x.off('click');
			x.click(function(e) {
				e.preventDefault();
				alert('requires ' + required + ' karma');
				return false;
			});
		}
	});
	if(!LOGGED_IN) {
		$('.requires-login').off('click');
		$('.requires-login').click(function(e) {
			e.preventDefault();
			alert('requires login');
			return false;
		});
	}
	$('.iframe-load').click(function() {
		var link = $(this);
		var iframe = $("<iframe/>");
		iframe.attr("src", link.attr("href"));
		link.parent().html(iframe);
	});
});
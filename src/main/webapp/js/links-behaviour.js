$(function() {
	if(!LOGGED_IN) {
		$('.requires-login').off('click');
		console.log('off');
		$('.requires-login').click(function(e) {
			e.preventDefault();
			alert('requires login');
			return false;
		});
	}
	$('.requires-karma').each(function(i, el) {
		var x = $(el);
		var required = parseInt(x.data("karma"));
		if(!MODERATOR && required > KARMA) {
			x.off('click');
			x.click(function() {
				alert('requires ' + required + ' karma');
			});
		}
	});
});
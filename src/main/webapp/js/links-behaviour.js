$(function() {
	$('.requires-karma').each(function(i, el) {
		var x = $(el);
		var required = parseInt(x.data("karma"));
		if(!MODERATOR && required > KARMA && x.data("author")=="false") {
			x.off('click');
			x.click(function(e) {
				e.preventDefault();
				errorDiv("Você precisa ter "+ required +" karma!", this);
			});
		}
	});
	if(!LOGGED_IN) {
		$('.requires-login').off('click');
		$('.requires-login').bind("click", function(e) {
			e.preventDefault();
			errorDiv("Você precisa estar logado!", this);
		});
	}
	$('.iframe-load').click(function() {
		var link = $(this);
		var iframe = $("<iframe/>");
		iframe.attr("src", link.attr("href"));
		link.parent().html(iframe);
	});
	
	function errorDiv(text, self){
		if($(".validation-error.popup").length == 0){
			var errorDiv = $("<div class='validation-error popup'>"+text+"</div>");
			errorDiv.appendTo($(self).parent()).show();
			$(errorDiv).click(function(){
				$(this).remove();
			});
		}
	}
});
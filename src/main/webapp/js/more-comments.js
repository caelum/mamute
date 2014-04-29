$('.more-comments').click(function(){
	var collapsed = $(this).siblings(".comment-list").find(".collapsed");
	collapsed.toggleClass("hidden");
	
	if(!collapsed.hasClass("hidden")) $(this).html("Ocultar comentários");
	else $(this).html(MESSAGES['show_all'] + " <strong>" + $(this).attr("size") + "</strong> " + MESSAGES['the_comments']);
});
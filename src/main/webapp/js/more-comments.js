$('.more-comments').click(function(){
	var collapsed = $(this).siblings(".comment-list").find(".collapsed");
	collapsed.toggleClass("hidden");
	
	if(!collapsed.hasClass("hidden")) $(this).html("Ocultar comentários");
	else $(this).html(Messages.get('show_all') + " <strong>" + $(this).attr("size") 
			+ "</strong> " + Messages.get('the_comments'));
});
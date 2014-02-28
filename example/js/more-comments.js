$('.more-comments').click(function(){
	var collapsed = $(this).siblings(".comment-list").find(".collapsed");
	collapsed.toggleClass("hidden");
	
	if(!collapsed.hasClass("hidden")) $(this).html("Ocultar comentários");
	else $(this).html("Mostrar todos os <strong>" + $(this).attr("size") + "</strong> comentários");
});
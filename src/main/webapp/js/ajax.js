$(function(){
	$(".link-ajax-post").click(function(event){
		event.preventDefault();
		$.post($(this).attr("href"));
	});
});
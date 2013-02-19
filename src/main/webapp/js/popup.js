$(function(){
	$(".show-popup").click(function(event){
		event.preventDefault();
		$(this).parent().find(".popup").toggle();
	});
	
	$(".close-popup").click(function(event){
		event.preventDefault();
		$(this).closest(".popup").hide();
	});
});
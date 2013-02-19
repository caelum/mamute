$(function(){
	$(".show-popup").click(function(e){
		e.preventDefault();
		$(this).parent().find(".popup").toggle();
	});
	
	$(".close-popup").click(function(e){
		e.preventDefault();
		$(this).closest(".popup").hide();
	});
});
$(function(){
	$(".show-popup").click(function(){
		$(this).parent().find(".popup").show();
	});
	
	$(".close-popup").click(function(e){
		e.preventDefault();
		$(this).closest(".popup").hide();
	});
});
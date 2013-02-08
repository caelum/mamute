$(function(){
	$(".show-popup").click(function(){
		$(this).parent().find(".popup").show();
	});
	
	$(".popup").click(function(){
		$(this).hide();
	});
});
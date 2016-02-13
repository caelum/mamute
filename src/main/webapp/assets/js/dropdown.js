$(function(){
	$(".dropdown-trigger").click(function(event){
		event.preventDefault();
		$("#"+$(this).data("target-id")).toggle();
	});
});
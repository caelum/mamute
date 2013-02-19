$(function(){
	$(".dropdown-trigger").click(function(event){
		event.preventDefault();
		console.log($("#"+$(this).data("target-id")));
		$("#"+$(this).data("target-id")).toggle();
	});
}) 
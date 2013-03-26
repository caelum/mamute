$(function(){
	$(".mark-as-solution").on('click', markAsSolution);
	
	function markAsSolution(event){
		event.preventDefault();
		var mark = $(this).find("span");
		$.ajax({
			url:$(this).attr("href"),
			method: "POST",
			success: function() {
				$(".solution-mark").removeClass("solution-mark");
				mark.addClass("solution-mark");
			}, 
			beforeSend: function() {
				mark.addClass("solution-mark");
			},
			error: function() {
				alert("Occorreu um erro");
			}
		});
	};
	
});
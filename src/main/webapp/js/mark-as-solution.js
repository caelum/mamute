$(function(){
	$(".mark-as-solution").on('click', markAsSolution);
	
	function markAsSolution(event){
		event.preventDefault();
		var mark = $(this);
		var oldSolution = $(".solution-mark");
		$.ajax({
			url:mark.attr("href"),
			method: "POST",
			beforeSend: function() {
				oldSolution.removeClass("solution-mark");
				mark.addClass("solution-mark");
			},
			error: function() {
				mark.removeClass("solution-mark")
				oldSolution.addClass("solution-mark");
				errorPopup("Ocorreu um erro...", mark, "center-popup");
			}
		});
	};
	
});
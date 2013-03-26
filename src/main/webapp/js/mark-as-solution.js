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
				if(!mark.hasClass("solution-mark")) oldSolution.toggleClass("solution-mark");
				mark.toggleClass("solution-mark");
			},
			error: function() {
				if(oldSolution != mark) oldSolution.toggleClass("solution-mark");
				mark.toggleClass("solution-mark")
				errorPopup("Ocorreu um erro...", mark, "center-popup");
			}
		});
	};
	
});
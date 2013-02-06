$(function(){
	$(".mark-as-solution").on('click', markAsSolution);
	
	function markAsSolution(event){
		event.preventDefault();
		var id = $(this).closest(".answer").data("id");
		var mark = $(this);
		$.post($(this).attr("href")+id,function(){updateMarks(mark)});
	};
	
	function updateMarks(markOfCurrentSolution){
		var solution = $(markOfCurrentSolution).closest(".answer"),
		otherAnswers = solution.siblings();
		
//		$(markOfCurrentSolution).hide();
		solution.addClass("solution");
		
		otherAnswers.removeClass("solution");
//		otherAnswers.find(".mark-as-solution").show();		
	}
	
});
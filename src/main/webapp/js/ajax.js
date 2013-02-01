$(function(){
	$(".mark-as-solution").on('click', markAsSolution);
	
	function markAsSolution(event){
		event.preventDefault();
		var id = $(this).closest(".answer").data("id");
		var mark = $(this);
		$.post($(this).attr("href")+id,function(){updateMarks(mark)});
	};
	
	function updateMarks(mark){
		var solution = $(mark).closest(".answer");
		solution.addClass("solution");
		$(mark).remove();
		$(".answer").not(solution).each(function(){
			console.log($(this));
			if($(this).hasClass("solution")){
				var newMark = mark.clone();
				newMark.on('click', markAsSolution);
				$(this).removeClass("solution")
						.append(newMark);
				
			}
		});
	}
	
});
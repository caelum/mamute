$(function(){
	$(".mark-as-solution").on('click', function(event){
		event.preventDefault();
		var id = $(this).closest(".answer").data("id");
		var mark = $(this);
		$.post($(this).attr("href")+id, function(data){
			$(mark).remove();
			$(".answer").each(function(){
				if($(this).hasClass("solution")){
					var newHref= mark.attr("href")+$(this).data("id");
					var newMark = mark.clone().attr("href", newHref);
					$(this).removeClass("solution")
							.append(newMark);
				}
			})
		});
	});
	
});
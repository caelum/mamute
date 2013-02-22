$(function(){
	$(".order-by").click(function(event){
		event.preventDefault();
		var self = $(this);
		$.get(self.attr("href"), function(list){
			console.log(self.data("type"));
			console.log(list);
			var listElements = ""
			$(list).each(function(index, item){
				if(self.data("type")=="question"){
					listElements += "<li><span>"+item.voteCount+"</span> <a href='#'>"+item.information.title+"</a></li>";
				}else if(self.data("type")=="answer"){
					listElements += "<li><span>"+item.voteCount+"</span> <a href='#'>"+item.question.information.title+"</a></li>";
				}
			});
			$("#"+self.data("target-id")).html(listElements);
		});
	});
});
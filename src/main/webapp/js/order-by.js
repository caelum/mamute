$(function(){
	$(".order-by").click(function(event){
		event.preventDefault();
		var self = $(this);
		$.get(self.attr("href"), function(list){
			var listElements = "";
			$(list).each(function(index, item){
				var question = getQuestion(self.data("type"), item),
					href = "/questions/"+question.id+"/"+question.information.sluggedTitle;
				
				listElements += "<li><span>"+item.voteCount+"</span> <a href='"+href+"'>"+question.information.title+"</a></li>";
			});
			$("#"+self.data("target-id")).html(listElements);
		});
	});
	
	function getQuestion(type, item){
		if(type == "questions"){
			return item;
		}else if(type == "answers"){
			return item.question;
		}
	}
});
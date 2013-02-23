$(function(){
	$(".order-by").click(function(event){
		event.preventDefault();
		var self = $(this);
		$.get(self.attr("href"), function(list){
			var listElements = "";
			$(list).each(function(index, item){
				var question = getQuestion(self.data("type"), item),
					href = getHref(self.data("type"), question, item);
				listElements += "<li><span>"+item.voteCount+"</span> <a href='"+href+"'>"+question.information.title+"</a></li>";
			});
			$("#"+self.data("target-id")).html(listElements);
		});
	});
	
	function getHref(type, question, item){
		var answerAnchor;
		if(type == "answers"){
			answerAnchor = "#answer-"+item.id;
		}
		return "/questions/"+question.id+"/"+question.information.sluggedTitle+answerAnchor;
	}
	
	function getQuestion(type, item){
		if(type == "questions"){
			return item;
		}else if(type == "answers"){
			return item.question;
		}
	}
});
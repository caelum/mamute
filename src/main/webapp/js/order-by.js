$(function(){
	$(".order-by").click(function(event){
		event.preventDefault();
		var self = $(this);
		$.get(self.attr("href"), function(list){
			repopulateWith("#"+self.data("target-id"), list, self.data("type"));
			selectMenu(self);
		});
	});
	
	function selectMenu(selectedMenu){
		$(selectedMenu).closest(".nav").find(".order-by").removeClass("selected");
		$(selectedMenu).addClass("selected");
	}
	
	function repopulateWith(target, list, type) {
		var listElements = "";
		$(list).each(function(index, item){
			var question = getQuestion(type, item),
				href = getHref(type, question, item);
			listElements += "<li class='ellipsis advanced-data-line'><span class='counter'>"+item.voteCount+"</span> <a href='"+href+"'>"+question.information.title+"</a></li>";
		});
		$(target).html(listElements);
	}
	
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
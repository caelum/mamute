$(function(){
	var ANSWER = "respostas",
		QUESTION = "perguntas"
		WATCHED = "acompanhadas";
	
	$(".order-by").click(function(event){
		event.preventDefault();
		var self = $(this);
		var href = self.attr("href");
		$.get(href, function(list){
			repopulateWith("#"+self.data("target-id"), list, self.data("type"));
			console.log(list);
			selectMenu(self);
			changePagerUrl(self, href);
		});
	});
	
	$(".advanced-user-data .pager a").click(function(event){
		event.preventDefault();
		var self = $(this);
		$.get(self.attr("href"), function(list) {
			repopulateWith("#"+self.data("target-id"), list, self.data("type"));
			selectPage(self.parent());
		});
	});
	
	function selectMenu(selectedMenu){
		$(selectedMenu).closest(".nav").find(".order-by").removeClass("selected");
		$(selectedMenu).addClass("selected");
	}
	
	function selectPage(pageSelected) {
		$(pageSelected).closest(".pager").find("li").removeClass("current");
		$(pageSelected).addClass("current");
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
		console.log(question);
		if(type == ANSWER){
			answerAnchor = "#answer-"+item.id;
		}
		return "/"+question.id+"-"+question.information.sluggedTitle+answerAnchor;
	}
	
	function getQuestion(type, item){
		if(type == QUESTION || type == WATCHED){
			return item;
		}else if(type == ANSWER){
			return item.question;
		}
	}
	
	function changePagerUrl(self, url){
		var pager = $(self).closest(".user-questions").find(".pager");
		pager.find("a").each(function(i,item){
			$(item).parent().removeClass("current");
			if(i == 0) $(item).parent().addClass("current");  
			$(item).attr("href", url + "&p=" + (i+1));
		});
	}
});
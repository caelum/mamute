$(function(){
	
	$(".advanced-data-section").on("click", ".order-by" , function(event){
		event.preventDefault();
		var self = $(this);
		var href = self.attr("href");
		var target = $("#" + self.data("target-id")).parent();
		var subheader = target.find(".subheader");
		selectMenu(self);
		var subheaderHTML = subheader[0].outerHTML;
		$.get(href, function(list){
			target.html(subheaderHTML + list);
			changePagerUrl(target, href);
		});
	});

	$(".advanced-data-section").on("click", ".pager a", function(event){
		event.preventDefault();
		var self = $(this);
		var target = $("#" + self.data("target-id")).parent();
		var subheader = target.find(".subheader")[0].outerHTML;
		$.get(self.attr("href"), function(list) {
			target.html(subheader + list);
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
	
	function changePagerUrl(self, url){
		var pager = $(self).closest(".user-questions").find(".pager");
		pager.find("a").each(function(i,item){
			var parent = $(item).parent();
			parent.removeClass("current");
			if(i == 0) parent.addClass("current");  
			$(item).attr("href", url + "&p=" + ($(item).html()));
		});
	}
});
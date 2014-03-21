$(".approve-news").click(function(e){
	e.preventDefault();
	var self = $(this);
	$.post(self.attr("href"), function(){
		self.closest(".post-item").removeClass("post-under-review");
		self.remove();		
	});
});
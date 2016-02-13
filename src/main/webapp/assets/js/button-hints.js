$(".button-hint").click(function(event){
	var self = $(this),
		target = $("#"+self.data("button-hint-id"));
	$(".button-hint-text").not(target).hide(100);
	$(".button-hint").not(self).removeClass("active");
	target.toggle(150);
	self.toggleClass("active");
	
});

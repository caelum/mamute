$(function(){
	$("body").on('click', '.show-popup', function(event){
		event.preventDefault();
		$(this).parent().find(".popup").toggle();
	});
	
	$("body").on('click', '.close-popup', function(event){
		event.preventDefault();
		var self = $(this)
		if(self.hasClass("popup")){
			self.remove();
		}else{
			self.closest(".popup").remove();
		}
	});
});
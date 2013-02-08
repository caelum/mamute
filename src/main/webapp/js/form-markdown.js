$(function() {
	$(".wmd-input").on('keyup', function(){
		var self = $(this),
		preview = self.closest(".wmd").find(".wmd-preview");
		if(self.val().length == 0){
			preview.addClass("hidden");
		}else{
			preview.removeClass("hidden");
		}
	});
	
	if($(".wmd-panel").length > 0) {
		var converter1 = Markdown.getSanitizingConverter();
		var editor1 = new Markdown.Editor(converter1);
		editor1.run();
	}
});
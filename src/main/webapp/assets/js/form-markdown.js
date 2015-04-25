$(function() {
	marked.setOptions({
	  sanitize: true
	});
	$(".wmd-input").on('keyup', function(){
		var self = $(this),
		preview = self.closest(".wmd").find(".md-preview");
		if(self.val().length == 0){
			preview.addClass("hidden");
		}else{
			preview.removeClass("hidden");
			preview.html(marked(self.val()));
		}
	});

	if($(".wmd-panel").length > 0) {
		var converter1 = Markdown.getSanitizingConverter();
		var editor1 = new Markdown.Editor(converter1);
		editor1.run();
        Globals.markdownEditor = editor1;
	}
	
});
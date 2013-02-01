$(function() {
	if($(".wmd-panel").length > 0) {
		var converter1 = Markdown.getSanitizingConverter();
		var editor1 = new Markdown.Editor(converter1);
		editor1.run();
	}
});
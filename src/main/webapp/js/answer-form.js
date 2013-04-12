$(function() {
	$(".answer-form textarea").focus(function() {
		var form = $(this).parents(".answer-form");
		if (form.data("same-author") && !form.data("warned")) {
			var confirm = $(".same-author-confirmation");
			var textarea = form.find("textarea");
			var submitButton=form.find("input[type='submit']");
			var markdownBar = $("#wmd-button-bar");
			
			textarea.parent().append(confirm);
			confirm.show();
			
			submitButton.addClass("opaque");
			markdownBar.addClass("opaque");
			confirm.find("button").click(function() {
				confirm.hide();
				textarea.show();
				markdownBar.removeClass("opaque");
				submitButton.removeClass("opaque");
			});
			textarea.hide();
			form.data("warned", true);
		}
	});
});
	

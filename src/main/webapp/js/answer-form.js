$(function() {
	$(".answer-form textarea").focus(function(e) {
		var form = $(this).parents(".answer-form");
		if (form.data("same-author") && !form.data("warned")) {
			var confirm = $(".same-author-confirmation");
			var textarea = form.find("textarea");
			var submitButton = form.find("input[type='submit']");
			var markdownBar = $("#wmd-button-bar");
			var inputs = form.find("input");
			
			textarea.parent().append(confirm);
			confirm.show();
			
			submitButton.addClass("opaque");
			markdownBar.addClass("opaque");
			inputs.attr("disabled", true);
			confirm.find("button").click(function() {
				confirm.hide();
				textarea.show();
				markdownBar.removeClass("opaque");
				submitButton.removeClass("opaque");
				inputs.attr("disabled", false);
				return false;
			});
			textarea.hide();
			form.data("warned", true);
		}
	});
});
	

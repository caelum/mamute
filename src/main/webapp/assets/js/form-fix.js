$("form").not(".ajax").submit(function(e) {
	var form = $(this)
	if (form.valid()) {
		var input = form.find("input[type=submit]");
		input.attr("disabled", "true");
		form.addClass("inactive");
	}
});

$(".history-select-version").change(function(event) {
	var selected = $(this).val();
	$(".history-form").addClass("hidden");
	$(".history-form").eq(selected).removeClass("hidden");
	$(".history-diff").addClass("hidden");
	$(".history-diff").eq(selected).removeClass("hidden");
});
$(".question-history-select").change(function(event) {
	var selected = $(this).val();
	$(".history-form").addClass("hidden");
	$(".history-form:eq("+selected+")").removeClass("hidden");
});
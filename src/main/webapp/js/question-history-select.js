$(".question-history-select").change(function(event) {
	var selected = $(this).val();
	$(".forms").addClass("hidden");
	$(".forms:eq("+selected+")").removeClass("hidden");
});
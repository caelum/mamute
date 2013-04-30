$(".history-select-version").change(function(event) {
	var selected = $(this).val();
	$(".history-forms-area").addClass("hidden").eq(selected).removeClass("hidden");
});

$(".toggle-version").click(function(){
	$(this).siblings('.history-version').toggleClass("hidden");
	$(this).siblings('.history-diff').toggleClass("hidden");
	$(this).siblings(".toggle-version").toggleClass("hidden");
	$(this).toggleClass("hidden");
});
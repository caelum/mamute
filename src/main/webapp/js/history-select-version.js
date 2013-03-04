$(".history-select-version").change(function(event) {
	var selected = $(this).val();
	$(".history-form").addClass("hidden");
	$(".history-form").eq(selected).removeClass("hidden");
	$(".history-diff").addClass("hidden");
	$(".history-diff").eq(selected).removeClass("hidden");
});

$(".toggle-original").click(function(){
	$(this).siblings('.post-text').first().toggleClass("hidden");
	$(this).siblings('.history-diff').toggleClass("hidden");
	$(this).siblings(".toggle-original").toggleClass("hidden");
	$(this).toggleClass("hidden");
});
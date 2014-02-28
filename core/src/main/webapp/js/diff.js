$(".history-form").each(function(i, item){
	$(".history-comparison").prettyTextDiff({
		originalContainer:".history-current .post-text",
		changedContainer: $(item).find(".history-version .post-text").first(),
		diffContainer:$(item).find(".history-diff.post-text").first(),
		cleanup:true
	});
});

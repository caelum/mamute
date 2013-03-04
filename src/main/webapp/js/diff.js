$(".history-form").each(function(i, item){
	$(".history-comparison").prettyTextDiff({
		originalContainer:".history-original .post-text",
		changedContainer: $(item).find(".post-text").eq(0),
		diffContainer:$(".history-diff").eq(i),
		cleanup:true
	});
});

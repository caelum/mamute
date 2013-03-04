$(".history-comparison").prettyTextDiff({
	originalContainer:".history-original .post-text",
	changedContainer:".history-version:not(.hidden) .post-text",
	diffContainer:".history-diff",
	cleanup:true
});
$(function() {
	$(".share-button").click(function() {
		var url = $(this).data("shareurl");
		window.open(url, "share", "width=650, height=450")
	});
});
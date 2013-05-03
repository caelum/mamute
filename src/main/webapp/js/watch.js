$(function(){
	$(".watch").on('click', watch);
	
	function watch(event){
		event.preventDefault();
		var post = $(this);
		var icon = post.find("span")
		
		$.ajax({
			url:post.attr("href"),
			method: "POST",
			beforeSend: function() {
				toggleClassOf(icon);
			},
			error: function() {
				errorPopup("Ocorreu um erro...", post, "center-popup");
			}
		});
		
		function toggleClassOf(icon){
			icon.toggleClass("icon-muted");
		}
	}
});
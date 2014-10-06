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
			error: function(jqXHR) {
				errorPopup(Messages.get('error.occured'), post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleClassOf(icon){
			icon.toggleClass("icon-muted");
			icon.toggleClass("icon-eye-off");
			icon.toggleClass("icon-eye");
			
			if(icon.hasClass("icon-muted")) {
				icon.attr("title", Messages.get('validation.follow_post'));
			} else {
				icon.attr("title", Messages.get('validation.cancel_follow_post'));
			}
		}
	}
});
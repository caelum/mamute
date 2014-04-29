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
				errorPopup(MESSAGES['error_occured'], post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleClassOf(icon){
			icon.toggleClass("icon-muted");
			icon.toggleClass("icon-eye-off");
			icon.toggleClass("icon-eye");
			
			if(icon.hasClass("icon-muted")) {
				icon.attr("title", MESSAGES['follow_post']);
			} else {
				icon.attr("title", MESSAGES['unfollow_post']);
			}
		}
	}
});
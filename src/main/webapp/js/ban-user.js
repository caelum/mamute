$(function(){
	$(".ban-user").on('click', toogleBan);
	
	function toogleBan(event){
		event.preventDefault();
		var post = $(this);
		
		$.ajax({
			url: post.attr("data-url"),
			method: "POST",
			beforeSend: function() {
				changeText(post.html());
			},
			error: function(jqXHR) {
				errorPopup(MESSAGES['error_occured'], post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function changeText(text){
			text == MESSAGES['block_user'] ? post.html(MESSAGES['unblock_user']) : post.html(MESSAGES['block_user']);
		}
	}
	
});
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
				errorPopup(Messages.get('error.occured'), post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function changeText(text){
			text == Messages.get('user.block_user') ? post.html(Messages.get('user.unblock_user')) : post.html(Messages.get('user.block_user'));
		}
	}
	
});
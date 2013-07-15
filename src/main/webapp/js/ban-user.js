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
				errorPopup("Ocorreu um erro...", post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function changeText(text){
			text == 'Bloquear usuário' ? post.html('Desbloquear usuário') : post.html('Bloquear usuário');
		}
	}
	
});
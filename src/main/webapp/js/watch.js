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
				errorPopup("Ocorreu um erro...", post, "center-popup");
				console.log(jqXHR);
			}
		});
		
		function toggleClassOf(icon){
			icon.toggleClass("icon-muted");
			icon.toggleClass("icon-eye-close");
			icon.toggleClass("icon-eye-open");
			
			if(icon.hasClass("icon-muted")) {
				icon.attr("title", "Clique aqui para ser notificado de novidades nessa questão e suas respostas.");
			} else {
				icon.attr("title", "Clique aqui para cancelar a notificação de novidades nessa questão e suas respostas.");
			}
		}
	}
});
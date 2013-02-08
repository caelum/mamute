$(function(){
	var url = window.location.href;
	
	$($('.nav-item').find('.button').get().reverse()).each(function(i, item){
		if(url.indexOf(item) >= 0) {
			$(item).css("background-color", "#F90");
			$('.replace').text($(item).text());		
			return false;
		}
	});
});

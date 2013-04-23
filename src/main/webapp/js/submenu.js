$(function(){
	var url = window.location.href;
	
	$($('.nav-item').find('.button').get().reverse()).each(function(i, item){
		if(url.indexOf(item) >= 0) {
			$(item).addClass("current");
			return false;
		}
	});
	
	$($('.tabs').find('a').get().reverse()).each(function(i, item){
		if(url.indexOf(item) >= 0) {
			$(item).addClass("selected");
			return false;
		}
	});
});

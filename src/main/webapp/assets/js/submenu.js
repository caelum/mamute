$(function(){
	var url = window.location.href;

	$($('.tabs').find('a').get().reverse()).each(function(i, item){
		if(url.indexOf(item) >= 0) {
			$(item).addClass("selected");
			return false;
		}
	});
});

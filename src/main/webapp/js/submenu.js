$(function(){
	var url = window.location.href;
	function byHrefLength(first, second){
		return $(second).attr("href").length - $(first).attr("href").length;
	}
	var navs = $('.nav-item .button').sort(byHrefLength);

	$(navs).each(function(i, item){
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

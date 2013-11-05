$(document).ready(function(){
	$(".main").onepage_scroll({
		sectionContainer: ".mediakit-section",
		loop:true,
		pagination: true,
		afterMove: currentLink,
		updateURL: true
	});
});

function currentLink(){
	var index = window.location.href.split("#")[1];
	var id = $(".mediakit-section[data-index*="+index+"]").attr("id");
	$(".nav-link").removeClass("current");
	var links = $("a[href*="+id+"]");
	$(links).addClass("current");
}

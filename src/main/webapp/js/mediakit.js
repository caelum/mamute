$(document).ready(function(){
	$(".main").onepage_scroll({
		sectionContainer: ".mediakit-section",
		loop:true,
		pagination: true,
		animationTime : 500,
		afterMove: home,
		beforeMove: home
	});
});

$(document).ready(home);

function home () {
	var id = $("section.active").attr("id");	
	var menu = $(".onepage-pagination");
	var links = $(".onepage-pagination li a");
	if(id=="home") {
		menu.addClass("color-dark");
		links.addClass("text");
	} else {
		menu.removeClass("color-dark");
		links.removeClass("text");
	}

}
$(document).ready(function(){
	$(".main").onepage_scroll({
		sectionContainer: ".mediakit-section",
		loop:true,
		pagination: false,
		afterMove: currentLink,
		updateURL: true
	});
});

$(".nav-link").on("click", function() {
	$(".mediakit-section").removeClass("active");
	var id = $(this).attr("href");
	$(id).addClass("active");
	$("body").removeClass().addClass("viewing-page-"+id.split("#")[1]);
});

function currentLink(){
	var index = window.location.href.split("#")[1];
	var id = $(".mediakit-section[data-index*="+index+"]").attr("id");
	$(".nav-link").removeClass("current");
	var links = $("a[href*="+id+"]");
	$(links).addClass("current");
}


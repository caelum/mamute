$(document).ready(function(){
	$(".main").onepage_scroll({
		sectionContainer: ".mediakit-section",
		loop:true,
		pagination: false
	});
});

$(".nav-link").on("click", function() {
	$(".mediakit-section").removeClass("active");
	var id = $(this).attr("href");
	$(id).addClass("active");
});

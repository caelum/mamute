manipulateBanner();

function manipulateBanner(){
	var banner = $(".about-banner");
	if(localStorage.minimizedBanner == "true") toggleBanner(banner);
}

$(".minimize-banner").click(function(){
	var banner = $(this).closest(".about-banner");
	toggleBanner(banner);
	localStorage.minimizedBanner = $(".minimized-banner")[0] != undefined;
});

function toggleBanner(banner){
	banner.find(".how-it-works").toggle(300);
	banner.find(".tell-me-more").toggleClass("minimized-banner");
}
$(".hide-next").click(function(){
	var hideButton = $(this)
    var toHide = $(hideButton.next());
    if (toHide.css("display") == "block"){
    	toHide.css("display", "none");
    	hideButton.children().attr("class", "icon-angle-right")
    }else{
    	toHide.css("display", "block");
    	hideButton.children().attr("class", "icon-angle-down")
    }
});

$(window).resize(function(){
	var hideButton = $(".hide-next");
	var toHide = $(hideButton.next());
	if ($(window).width() >= 501){	
		if (toHide.css("display") == "none" ){
			toHide.css("display", "inline-block");
		}
	}else{
		toHide.css("display", "none");
		hideButton.children().attr("class", "icon-angle-right");
	}
});
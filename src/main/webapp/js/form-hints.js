$(function(){
	$(".hintable").each(function(index, hintable){
		var currentHintable = $(hintable),
			anchor = currentHintable.data("hint-id"),
			label = $("label[for="+currentHintable.attr("id")+"]"),
			hintAnchor = $("<a class='hint-anchor' href='#"+anchor+"'> ?</a>");
		hintAnchor.appendTo(label);
	});
	
	var toggleHintFor = function(element) {
		var hint = $("#" + $(element).data("hint-id"));
		if(!$(hint).is(":visible")){
			$(".hint").hide();
			hint.fadeIn(500);
		}
	};
	
	$(".hintable").focus(function() {
		toggleHintFor(this);
	});
	
	$(".hintable").focusout(function() {
		var hint = $(".answer-form > .hint");
		hint.fadeOut(500);
	});
	
	$('#question-title').focus();
});
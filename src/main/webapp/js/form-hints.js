$(function(){
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
	$('#question-title').focus();
});
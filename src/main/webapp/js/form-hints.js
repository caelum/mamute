$(function(){
	var showHintFor = function(element) {
		var hint = $("#" + $(element).data("hint-id"));
		hint.fadeIn(500);
	};
	var hideHintFor = function(element) {
		var hint = $("#" + $(element).data("hint-id"));
		hint.hide();
	};
	$(".hintable").focus(function() {
		showHintFor(this);
	});
	$(".hintable").blur(function() {
		hideHintFor(this);
	});
	$('#question-title').focus();
});
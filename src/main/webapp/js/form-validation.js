$(function(){
	$(".validated-form").validate();
});

$(".answerForm").validate({
	rules: {
		"answerText": {
			required: true,
			minlength: 15
		}
	}
});
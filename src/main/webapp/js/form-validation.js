$(function(){
	$(".validated-form").validate();
});

$(".answerForm").validate({
	rules: {
		"answer.text": {
			required: true,
			minlength: 15
		}
	}
});
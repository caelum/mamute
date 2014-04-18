$(document).on('keyup', '.comment-textarea', commentLengthCounter);

function commentLengthCounter() {
	var length = $('.comment-textarea').val().length;
	$('.comment-length-counter').html(" " + (600 - length));
}
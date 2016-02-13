$(document).on('keyup', '.comment-textarea', commentLengthCounter);

function commentLengthCounter(textarea) {
	var length;
	
	if( this !== window )
		length = $(this).val().length;
	else
		length = $(textarea).val().length;
	
	$('.comment-length-counter').html(" " + (600 - length));
}
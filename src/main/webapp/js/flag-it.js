$('.comment').hover(function(){
	var commentOptions = $(this).find('.comment-options');
	var flag = commentOptions.find('.flag-it');
	var up = commentOptions.find('.vote-option');
	if(!flag.hasClass('flag-selected')) {
		flag.toggleClass('important-hidden');
		up.toggleClass('important-hidden');
	}
});

$(".flag-it").click(function(e) {
	e.preventDefault();
	var link = $(this);
	var commentOptions = link.parent();
	var comment = link.parents('.comment');
	var modal = commentOptions.siblings(".modal");
	var form = modal.find("form");
	var uri = form.attr("action");
	
	modal.toggleClass('hidden');
	
	if(modal.hasClass('hidden')) {
		link.removeClass('flag-selected');
		comment.removeClass('to-flag');
	} else {
		link.addClass('flag-selected');
		comment.addClass('to-flag');
	}
	
	var callbacks = {};
	callbacks["409"] = function() {
		alert("conflict, you can't do it, sorry");
	};
	callbacks["400"] = function() {
		alert("please choose an valid option");
	};
	callbacks["403"] = function() {
		alert("you must login");
	};
	callbacks["200"] = function() {
		modal.hide(200);
		link.remove();
	};
	
	form.submit(function(e) {
		e.preventDefault();
		$.ajax(uri, {
			complete: function(xhr, textStatus) {
				console.log(xhr.status);
				if (callbacks[xhr.status] != undefined) {
					callbacks[xhr.status].call();
				}
				else {
					alert("something went wrong");
				}
			},
			data: form.serialize(),
			headers: {Accept: "application/json"},
			method: "POST"
		});
	});
	
});

$(".other-option").change(function() {
	var self = $(this);
	self.siblings("#other-reason").show(200);
});

$(".modal input:not(.other-option)").change(function() {
	$(".modal #other-reason").hide();
});
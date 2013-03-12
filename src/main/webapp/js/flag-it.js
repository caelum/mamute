
$(".flag-it").click(function(e) {
	e.preventDefault();
	var link = $(this);
	var commentOptions = link.parent();
	var comment = link.parents('.comment');
	var modal = comment.find(".modal-flag");
	var form = modal.find("form");
	var uri = form.attr("action");
	
	modal.toggleClass('hidden');
	link.toggleClass('selected');
	comment.toggleClass('to-flag');
	
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
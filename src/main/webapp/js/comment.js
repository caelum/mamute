$(function() {
	$('.edit-via-ajax a').click(function(e) {
		e.preventDefault();
		var father = $(this).parent();
		var child = father.children();
		child.toggle();
		father.find(".to-focus").focus();
		return false;
	});
	$('form.ajax').submit(function(e) {
		e.preventDefault();
		var self = $(this);

		var error = function() {
			console.log("error");
		};

		var success = function(response) {
			var el = self.parent(".edit-via-ajax");
			console.log(el.html());
			el.html(response);
		};

		var uri = self.attr("action");
		$.ajax(uri, {
			success: success,
			error: error,
			data : self.serialize(),
			method: "POST"
		});
		
		return false;
	});
});
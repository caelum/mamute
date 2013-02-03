$(function() {
	$('.add-a-comment a').click(function(e) {
		e.preventDefault();
		var father = $(this).parent();
		var child = father.children();
		child.toggle();
		father.find("textarea").focus();
		return false;
	});
	$('form.ajax').submit(function(e) {
		e.preventDefault();
		var self = $(this);

		var error = function() {
			console.log("error");
		};

		var success = function() {
			self.parent().parent().html(self.find("textarea").val());
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
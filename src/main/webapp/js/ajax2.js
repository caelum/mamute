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

		var success = function(response, status, jqhr) {
			console.log(jqhr);
			var target = $("#" + self.data("ajax-result"));
			if(jqhr.status==201) {
				target.append("<span class='suggestion-accepted'>Sugest&atilde;o enviada!</span>");
			} else {
				var action = self.data("ajax-on-callback") || "replace";
				if(action == "replace") {
					target.html(response);
				} else if(action == "append") {
					target.append(response);
				}
			}
			var formParent = self.closest(".edit-via-ajax");
			formParent.children().toggle();
		};

		var uri = self.attr("action");
		$.ajax(uri, {
			success: success,
			error: error,
			dataType : 'html',
			data : self.serialize(),
			method: "POST"
		});
		
		return false;
	});
});
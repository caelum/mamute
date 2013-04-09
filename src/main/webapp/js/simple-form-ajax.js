$(function() {
	$('body').on("click", ".simple-ajax-form a", function(e) {
		e.preventDefault();
		var father = $(this).parent();
		var child = father.children();
		child.toggle();
		father.find(".to-focus").focus();
	});

	$('body').on("click", ".simple-ajax-form .cancel", function(e) {
		e.preventDefault();
		var father = $(this).closest(".simple-ajax-form").addClass("hidden");
	});
	
	$('body').on("submit", "form.ajax", function(e) {
		e.preventDefault();
		var self = $(this);
		if(!self.valid()) return false;

		var error = function() {
			console.log("error");
		};

		var success = function(response, status, jqhr) {
			var target = $("#" + self.data("ajax-result"));
			if(jqhr.status==201) {
				target.append("<span class='suggestion-accepted'>Sugest&atilde;o enviada!</span>");
			} else {
				var action = self.data("ajax-on-callback") || "replace-inner";
				if(action == "replace-inner") {
					target.html(response);
				} else if(action == "append") {
					target.append(response);
				} else if(action == "replace"){
					target.replaceWith(response);
				}
			}
			self.find("textarea").val("");
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
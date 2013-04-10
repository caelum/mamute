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
		var form = $(this);
		executeAjax(form);

	});
	
	function executeAjax(form){
		if(!form.valid()||form.hasClass("inactive")) return false;
		form.addClass("inactive");

		var error = function() {
			errorPopup("Ocorreu um erro.", form, "center-popup");
			form.removeClass("inactive").addClass("hidden");
		};
	
		var success = function(response, status, jqhr) {
			var target = $("#" + form.data("ajax-result"));
			if(jqhr.status==201) {
				target.append("<span class='suggestion-accepted'>Sugest&atilde;o enviada!</span>");
			} else {
				var action = form.data("ajax-on-callback") || "replace-inner";
				if(action == "replace-inner") {
					target.html(response);
				} else if(action == "append") {
					target.append(response);
				} else if(action == "replace"){
					target.replaceWith(response);
				}
				target.removeClass("hidden");
			}
			form.find("textarea").val("");
			var formParent = form.closest(".edit-via-ajax");
			formParent.children().toggle();
			form.removeClass("inactive").addClass("hidden");
		};
	
		var uri = form.attr("action");
		$.ajax(uri, {
			success: success,
			error: error,
			dataType : 'html',
			data : form.serialize(),
			method: "POST"
		});
		return false;
	}
});
	

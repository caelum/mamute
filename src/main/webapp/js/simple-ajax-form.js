$(function() {
	bindAll();
	function bindAll() {
		$(".simple-ajax-form a").on("click", showForm);
		$(".simple-ajax-form .cancel").on("click", hideForm);
		$("form.ajax").on("submit", submitForm);
	}
	
	function showForm(e){
		e.preventDefault();
		var formArea = $(this).siblings(".ajax-form");
		formArea.toggleClass("hidden");
		formArea.find(".to-focus").focus();
	}
	
	function hideForm(e){
		e.preventDefault();
		var form= $(this).closest("form.ajax");
		resetForm(form)
	}
	
	function submitForm(e) {
		e.preventDefault();
		executeAjax($(this));
	}
	
	function resetForm(form){
		var formParent = form.parent();
		form.removeClass("inactive").find("textarea").val("");
		formParent.addClass("hidden");
	}
	
	function executeAjax(form){
		if (!form.valid() || form.hasClass("inactive")) 
			return;
		form.addClass("inactive");

		var error = function(jqXHR) {
			form.removeClass("inactive").addClass("hidden");
			if (jqXHR.status == 400) {
				errorPopup("Ocorreu um erro de validação inesperado.", form, "center-popup");
				return;
			}
			errorPopup("Ocorreu um erro.", form, "center-popup");
			resetForm(form);
		};
	
		var success = function(response, status, jqhr) {
			var target = $("#" + form.data("ajax-result"));
			if (jqhr.status == 201) {
				target.append("<span class='suggestion-accepted'>Sugest&atilde;o enviada!</span>");
			} else {
				var action = form.data("ajax-on-callback") || "replace-inner";
				if (action == "replace-inner") {
					target.html(response);
				} else if(action == "append") {
					target.append(response);
				} else if(action == "replace"){
					target.replaceWith(response);
				}
				target.removeClass("hidden");
			}
			resetForm(form);
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
	}
});
	

$(function() {
	bindAll();
	
	function bindAll(){
		bind($(".simple-ajax-form a"), "click", showForm);
		bind($(".simple-ajax-form .cancel"), "click", hideForm);
		bind($('form.ajax'), "submit", submitForm);
	}

	function bind(element, event ,callback){
		element.off(event, callback);
		element.on(event, callback);
	}
	
	function showForm(e) {
		e.preventDefault();
		var formArea = $(this).siblings(".ajax-form");
		formArea.toggleClass("hidden");
		formArea.find(".to-focus").focus();
		commentLengthCounter(formArea.find('form textarea'));
	}
	
	function hideForm(e){
		e.preventDefault();
		var form = $(this).closest("form.ajax");
		resetForm(form, true);
	}
	
	function submitForm(e) {
		e.preventDefault();
		executeAjax($(this));
	}
	
	function resetForm(form, cancelPressed){
		var formParent = form.parent();
		form.removeClass("inactive");
		form.find("input[type='submit']").attr("disabled", false);
		formParent.addClass("hidden");
		if(!cancelPressed)
			form.find(".comment-textarea").val("");
	}
	
	function executeAjax(form){
		if (!form.valid()) 
			return;

		form.find(".submit").attr("disabled", true);
		form.find(".cancel").attr("disabled", false);
		
		var error = function(jqXHR) {
			resetForm(form, false);
			if (jqXHR.status == 400) {
				errorPopup(Messages.get('error.occured'), form.parent(), "center-popup");
				return;
			}
			errorPopup(Messages.get('error.occured'), form.parent(), "center-popup");
			console.log(jqXHR);
		};
	
		var success = function(response, status, jqhr) {
			var target = $("#" + form.data("ajax-result"));
			
			var action = form.data("ajax-on-callback") || "replace-inner";
			if (action == "replace-inner") {
				target.html(response);
			} else if(action == "append") {
				target.append(response);
			} else if(action == "replace"){
				target.replaceWith(response);
			}
			target.removeClass("hidden");
			
			resetForm(form, false);
			bindAll();
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
	

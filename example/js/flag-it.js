$(".flag-it").click(function(e) {
	e.preventDefault();
	var link = $(this);
	var commentOptions = link.parent();
	var comment = link.parents('.comment');
	var modal = new Modal($("#" + link.data("modal-id")));
	var form = modal.element.find("form");
	var uri = form.attr("action");

	link.toggleClass('selected');
	comment.toggleClass('to-flag');

	var callbacks = {};
	callbacks["409"] = function() {
		errorPopup("Você não pode fazer isso.", modal.element, "center-popup");
	};
	callbacks["400"] = function() {
		errorPopup("Escolha uma opção.", modal.element, "center-popup");
	};
	callbacks["403"] = function() {
		errorPopup("Você deve estar logado.", modal.element, "center-popup");
	};
	callbacks["200"] = function() {
		modal.hide(200);
		link.remove();
	};
	
	var errors = form.find(".error");
	
	form.change(function(){
		errors.text("");
	});

	form.submit(function(e) {
		e.preventDefault();
		var checked = form.find("input:radio:checked");
		if (isEmpty(checked)) {
			errors.text("Escolha um motivo").show();
			return;
		}
		reason = form.find("textarea");
		if (checked.val() == "OTHER" && isEmpty(reason.val())) {
			errors.text("Descreva o motivo").show();
			return;
		}
		$.ajax(uri, {
			complete : function(xhr, textStatus) {
				if (callbacks[xhr.status] != undefined) {
					callbacks[xhr.status].call();
				} else {
					errorPopup("Ocorreu um erro.", modal, "center-popup");
					console.log(xhr);
				}
			},
			data : form.serialize(),
			headers : {
				Accept : "application/json"
			},
			method : "POST"
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

function isEmpty(el) {
	return el.length == 0;
}
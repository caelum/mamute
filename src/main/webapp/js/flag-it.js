$(".flag-it").click(function(e) {
	e.preventDefault();
	var link = $(this);
	var modal = link.siblings(".modal");
	var form = modal.find("form");
	var uri = form.attr("action");
	
	modal.show(200);
	form.submit(function(e) {
		e.preventDefault();
		$.ajax(uri, {
			success: function() {alert("deu certo")},
			error: function() {alert("deu errado")},
			data: form.serialize(),
			method: "POST"
		});
	});
	
});
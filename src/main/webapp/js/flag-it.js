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
			success: function() {
				modal.hide(200);
				link.remove();
			},
			error: function(xhr, textStatus) {
				if (xhr.status == "403") {
					alert("sorry, you can't do it");
				} else if (xhr.status == "400") {
					alert("please choose an valid option");
				}
				else { 
					alert("something went wrong");
				}
			},
			data: form.serialize(),
			method: "POST"
		});
	});
	
});

$(".other-option").change(function() {
	$(this).next("textarea").show(200);
});

$(".modal input:not(.other-option)").change(function() {
	$(".modal textarea").hide();
});
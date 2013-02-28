$(".vote-option").bind("click", function() {
	if (!$(this).hasClass("voted")) vote($(this));
});

function vote(link) {
	var vote = link.data("value");
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/"+ vote;
	$.ajax(""+ params, {
		complete: function(jqXHR, textStatus) {
			if (jqXHR.status == "200") {
				var count = jqXHR.responseText;
				voteSuccess(link, count);
			} else if (jqXHR.status == "403") {
				errorPopup("Você não pode votar na própria questão", link);
			} else {
				errorPopup("Você precisa logar", link);
			}
		},
		accepts: "application/json",
		error: voteError,
		method: "POST"
	});
}

function highlight(link) {
	link.addClass("voted").siblings().removeClass("voted");
}

function updateCount(link, count) {
	var voteCount = $(link).closest(".vote-container").find(".vote-count");
	voteCount.text(count);
}

function voteSuccess(link, count) {
	highlight(link);
	updateCount(link, count);
}

function voteError() {
	console.log("falhou");
}

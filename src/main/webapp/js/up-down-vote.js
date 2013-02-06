$(".vote-option").bind("click", function() {
	var alreadyVoted = $(this).closest(".vote").find(".already-voted");
	if ($(this).hasClass("voted")){
		alreadyVoted.show();
	} else {
		alreadyVoted.hide();
		vote($(this));
	}
});

function vote(link) {
	var vote = link.data("value");
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/"+ vote;
	$.ajax(""+ params, {
		complete: function(jqXHR, textStatus) {
			console.log(jqXHR.status);
			if (jqXHR.status == "200") {
				var count = jqXHR.responseText;
				voteSuccess(link, count);
			} else if (jqXHR.status == "403") {
				alert("you can't vote on your own question");
			} else {
				alert("you must login");
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
	var voteCount = $(link).closest(".vote").find(".vote-count");
	voteCount.text(count);
}

function voteSuccess(link, count) {
	highlight(link);
	updateCount(link, count);
}

function voteError() {
	console.log("falhou");
}

$(".vote-option").bind("click", function() {
	var alreadyVoted = $(this).closest(".vote").find(".already-voted");
	if($(this).hasClass("voted")){
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
		success: function(count) { voteSuccess(link, count) },
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

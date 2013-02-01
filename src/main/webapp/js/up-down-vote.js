$(".up-vote").bind("click", function() {
	vote("up", $(this));
});

$(".down-vote").bind("click", function() {
	vote("down", $(this));
});

function vote(vote, link) {
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/"+ vote;
	$.ajax(""+ params, {
		success: function() { voteSuccess(link) },
		error: voteError,
		method: "POST"
	});
}

function highlight(link) {
	link.addClass("voted").siblings().removeClass("voted");
}

function voteSuccess(link) {
	highlight(link);
}

function voteError() {
	console.log("falhou");
}

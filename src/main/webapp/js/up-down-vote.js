$(".vote-option").bind("click", function() {
	if (!$(this).hasClass("voted")) vote($(this));
});

function vote(link) {
	var vote = link.data("value");
	var type = link.data("type");
	var id = link.data("id");
	var params = "/"+ type +"/"+ id +"/voto/"+ vote;

	fakeUpdate(link, vote);
	
	$.ajax(""+ params, {
		complete: function(jqXHR, textStatus) {
			if (jqXHR.status == "200") {
				var count = jqXHR.responseText;
				voteSuccess(link, count);
			} else if (jqXHR.status == "409") {
				errorPopup("Você não pode votar na própria questão", link);
			} else if (jqXHR.status == "403") {
				errorPopup(jqXHR.responseText, link);
			} else {
				errorPopup("Ocorreu um erro", link);
				console.log(jqXHR);
			}
		},
		accepts: "application/json",
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

function fakeUpdate(link, vote) {
	var value;
	var countWithoutAjax = parseInt($(link).closest(".vote-container").find(".vote-count").html());
	var alreadyVoted = link.siblings().hasClass("voted");
	
	if(vote == "positivo") value = countWithoutAjax + 1 + alreadyVoted;
	else value = countWithoutAjax - 1 - alreadyVoted;
	
	voteSuccess(link, value);
}


function voteSuccess(link, count) {
	highlight(link);
	updateCount(link, count);
}


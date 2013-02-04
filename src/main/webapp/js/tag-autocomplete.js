var autoCompleteId;
$(".autocomplete").keyup(function(){
	var autoCompleteInput = $(this),
	target = $("#"+autoCompleteInput.data("autocomplete-id")),
	input = $(autoCompleteInput.val().split(" ")).last().get(0);
	clearTimeout(autoCompleteId);
	autoCompleteId = setTimeout(function(){suggestsAutoComplete(target, input)},500);
});

function suggestsAutoComplete(target, input){
	$.get("/tagsLike/"+input,function(suggestions){
		var suggestionElements = "";
		$(suggestions).each(function(index, suggestion){
			suggestionElements += "<dt>"+suggestion.name+"</dt>";
			suggestionElements += "<dd>"+suggestion.description+"</dd>";
		});
		$(target).html(suggestionElements);
	});
}
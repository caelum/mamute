var autoCompleteId;
$(".autocomplete").keyup(function(){
	var autoCompleteInput = $(this),
	target = $("#"+autoCompleteInput.data("autocomplete-id")),
	input = $(autoCompleteInput.val().split(" ")).last().get(0);
	clearTimeout(autoCompleteId);
	autoCompleteId = setTimeout(function(){suggestsAutoComplete(target, input)},500);
});

function suggestsAutoComplete(target, input){
	if(input == undefined || input == " " || !input) return;
	$.get("/tagsLike/"+input,function(suggestions){
		var suggestionElements = "";
		$(suggestions).each(function(index, suggestion){
			suggestionElements += "<dt>"+suggestion.name+"</dt>";
			suggestionElements += "<dd>"+suggestion.description+"</dd>";
		});
		$(target).html(suggestionElements);
	});
}

$('#newquestion-tags-autocomplete').click(function() {
	var input =$('input[name=tagNames]');
    var inputValue = input.val();
    var vetValue = inputValue.split(" ");
    vetValue = vetValue.slice(0, vetValue.length - 1);
    vetValue.push($(this).children(':first').text());
    input.val(vetValue.join(' ')+" ");
    input.focus();
}
);
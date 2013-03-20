var autoCompleteId;
$(".autocomplete").keyup(function(){
	var autoCompleteInput = $(this),
	target = $("#"+autoCompleteInput.data("autocomplete-id")),
	tagChunk = $(autoCompleteInput.val().split(" ")).last().get(0);
	clearTimeout(autoCompleteId);
	autoCompleteId = setTimeout(function(){suggestsAutoComplete(target, tagChunk, autoCompleteInput)},500);
});

$("*:not(.autocomplete)").click(function(){
	$(".autocompleted-tags").addClass("hidden");
});

function suggestsAutoComplete(target, tagChunk, input){
	if(tagChunk == undefined || tagChunk == " " || !tagChunk) return;
	$.get("/tags-similares/"+tagChunk,function(suggestions){
		if(suggestions.length > 0){
			showSuggestions(suggestions, target);
		} 
	});
}

function showSuggestions(suggestions, target){
	var suggestionElements = "";
	$(suggestions).each(function(index, suggestion){
		suggestionElements += "<li class='complete-tag'><a class='tag'>"+suggestion.name+"</a> x "+ suggestion.usageCount;
		suggestionElements += "<div class='tag-description'>"+suggestion.description+"</div></li>";
	});
	$(target).html(suggestionElements).removeClass("hidden");
	$('.autocompleted-tags .complete-tag').click(function(){insertTagIntoTextArea($(this).find(".tag").text())});
}


function insertTagIntoTextArea(text) {
	var input =$('input[name=tagNames]');
    var inputValue = input.val();
    var vetValue = inputValue.split(" ");
    vetValue = vetValue.slice(0, vetValue.length - 1);
    vetValue.push(text);
    input.val(vetValue.join(' ')+" ");
    $(input).valid();
    input.focus();
}

var autoCompleteId;
$(".autocomplete").keyup(function(){
	var autoCompleteInput = $(this),
	target = $("#"+autoCompleteInput.data("autocomplete-id")),
	input = $(autoCompleteInput.val().split(" ")).last().get(0);
	clearTimeout(autoCompleteId);
	autoCompleteId = setTimeout(function(){suggestsAutoComplete(target, input)},500);
});

$("*:not(.autocomplete)").click(function(){
	$(".autocompleted-tags").removeClass("active");
});

function suggestsAutoComplete(target, input){
	if(input == undefined || input == " " || !input) return;
	$.get("/tagsLike/"+input,function(suggestions){
		if(suggestions.length > 0){
			var suggestionElements = "";
			$(suggestions).each(function(index, suggestion){
				suggestionElements += "<li><a class='tag autocompleted-tag'>"+suggestion.tag.name+"</a> x "+ suggestion.usage;
				suggestionElements += "<div class='tag-description'>"+suggestion.tag.description+"</div></li>";
			});
			$(target).html(suggestionElements).addClass("active");
			$('.autocompleted-tags li').click(function(){insertTagIntoTextArea($(this).find(".tag").text())});
		}
	});
}

function insertTagIntoTextArea(text) {
	var input =$('input[name=tagNames]');
    var inputValue = input.val();
    var vetValue = inputValue.split(" ");
    vetValue = vetValue.slice(0, vetValue.length - 1);
    vetValue.push(text);
    input.val(vetValue.join(' ')+" ");
    input.focus();
}

var autoCompleteId;
$('.autocomplete').keypress(function(){
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

var pos=-3;
$('.autocomplete').keydown(function(e){
	arrow = {left: 37, up: 38, right: 39, down: 40};
	control = {esc: 27, enter: 13};
	var completeTag = $('.complete-tag');
	
	switch(e.which) {
		case arrow.down:
			if(pos < completeTag.length - 3) pos += 3;
			break;
			
		case arrow.up:
			if(pos > 0) pos -= 3;
			break;
			
		case arrow.right:
			if(pos < completeTag.length - 1) pos++;
			break;
			
		case arrow.left:
			if(pos > 0)	pos--;
			break;
			
		case control.esc:
			$('.autocompleted-tags').addClass('hidden');
			break;
			
		case control.enter:
			insertTagIntoTextArea(completeTag.eq(pos).find(".tag").text());
			pos=-3;
			
		default: return;
	}

	completeTag.removeClass('tag-selected');
	completeTag.eq(pos).addClass('tag-selected');
	e.preventDefault();
});

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

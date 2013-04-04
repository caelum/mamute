var autoCompleteId;
$('.autocomplete').keyup(function(e){
	var autoCompleteInput = $(this),
	target = $("#"+autoCompleteInput.data("autocomplete-id")),
	tagChunk = $(autoCompleteInput.val().split(" ")).last().get(0);
	
	if(!tagChunk){
		target.addClass("hidden");
		return;
	}
		
	if(isNotAControl(e.which)) { 
		showAutoCompleteArea(target);
		clearTimeout(autoCompleteId);
		autoCompleteId = setTimeout(function(){suggestsAutoComplete(target, tagChunk, autoCompleteInput)},100);
	}
});

function isNotAControl(key){
	keyboardCtrlAutoCompleteBox = [13, 27, 37, 38, 39, 40];
	return $.inArray(key, keyboardCtrlAutoCompleteBox) < 0
}

function showAutoCompleteArea(target){
	target.removeClass("hidden");
	setLoading(target);
}

$("*:not(.autocomplete)").click(function(){
	$(".autocompleted-tags").addClass("hidden");
});

function suggestsAutoComplete(target, tagChunk, input){
	if(tagChunk == undefined || tagChunk == " " || !tagChunk) return;
	suggestions = getSuggestions(tagChunk);
	showSuggestions(suggestions, target);
}

function getSuggestions(tagChunk){
	var regex = new RegExp(".*"+tagChunk+".*");
	var suggestions = $(ALL_TAGS).map(function(index, tag){
		if(tag.name.match(regex)){
			return tag;
		}
	});
	return suggestions.slice(0, 9);
}

function showSuggestions(suggestions, target){
	if(suggestions.length != 0){
		var suggestionElements = "";
		$(suggestions).each(function(index, suggestion){
			suggestionElements += "<li class='complete-tag'><a class='tag-brutal'>"+suggestion.name+"</a> x "+ suggestion.usageCount;
			suggestionElements += "<div class='tag-description'>"+suggestion.description+"</div></li>";
		});
		$(target).html(suggestionElements).removeClass("hidden");
		
		$('.autocompleted-tags .complete-tag').click(function(){
			var self = $(this);
			insertTagIntoTextArea(self.find(".tag-brutal").text());
		});
	}else{
		target.addClass("hidden");
	}
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
			
		case control.enter:
			e.preventDefault();
			completeTag.eq(pos).click();
			pos=-3;
			$('.autocompleted-tags').addClass('hidden');
			
		case control.esc:
			$('.autocompleted-tags').addClass('hidden');
			break;
			
		default: return;
	}

	completeTag.removeClass('tag-selected');
	completeTag.eq(pos).addClass('tag-selected');
});

function insertTagIntoTextArea(text) {
	var input = $('input[name=tagNames]');
    var inputValue = input.val();
    var vetValue = inputValue.split(" ");
    vetValue = vetValue.slice(0, vetValue.length - 1); // remove the last tag(that is incomplete)
    vetValue.push(text);
    input.val(vetValue.join(' ')+" ");
    $(input).valid();
    input.focus();
    $(".autocompleted-tags").html("");
}

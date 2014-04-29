(function() {

	var autoCompleteId;
	var TagsManager = function(components){

		$("*:not(.autocomplete)").click(function(){
			$(".autocompleted-tags").addClass("hidden");
		});

		$.getJSON(ALL_TAGS_URL, function(json){
			var allTags = json;

			var element = $('.autocomplete');
			for ( var i in components) {
				var component = components[i];
				component.forElement(element, allTags);
			}
		});
	}

	var AutoCompleteDOM = function(){
		var splitChar = TAGS_SPLITTER_CHAR;

		var showAutoCompleteArea = function(target){
			target.removeClass("hidden");
			setLoading(target);
		}

		var suggestAutoComplete = function(target, tagChunk, input, allTags){
			if(tagChunk == undefined || tagChunk == splitChar || !tagChunk) return;
			suggestions = getSuggestions(tagChunk, allTags);
			showSuggestions(suggestions, target);
		}

		function getSuggestions(tagChunk, allTags){
			var regex = new RegExp(".*"+tagChunk+".*");
			var suggestions = $(allTags).map(function(index, tag){
				if(tag.name.match(regex)){
					return tag;
				}
			});

			return sortAndTrim(suggestions, tagChunk);
		}

		function sortAndTrim(array, tagChunk){
			return array.sort(function(tagA, tagB){
				return  tagA.name.indexOf(tagChunk) - tagB.name.indexOf(tagChunk);
			}).slice(0, 9);
		}

		var showSuggestions = function(suggestions, target){
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

		var insertTagIntoTextArea = function(text) {
			var input = $('input[name=tagNames]');
		    var inputValue = input.val();
		    var vetValue = inputValue.split(splitChar);
		    vetValue = vetValue.slice(0, vetValue.length - 1); // remove the last tag(that is incomplete)
		    vetValue.push(text);
		    input.val(vetValue.join(splitChar)+splitChar);
		    $(input).valid();
		    input.focus();
		    $(".autocompleted-tags").html("");
		}

		function isNotAControl(key){
			keyboardCtrlAutoCompleteBox = [13, 27, 37, 38, 39, 40];
			return $.inArray(key, keyboardCtrlAutoCompleteBox) < 0
		}

		function escapeSpecialCharacter(tagChunk){
		   specialCharacters = ['+','.','|','$','*','^','(',')'];
		   pattern = specialCharacters.join("");
		   tagChunk = tagChunk.replace(new RegExp("([" + pattern + "])", 'g'), "\\$1");
		   return tagChunk;
		}

		return {
			forElement: function(element, allTags){
				element.keyup(function(e){
					var autoCompleteInput = $(this),
					target = $("#"+autoCompleteInput.data("autocomplete-id")),
					tagChunk = $(autoCompleteInput.val().split(splitChar)).last().get(0);

					if(!tagChunk){
						target.addClass("hidden");
						return;
					}


					if(isNotAControl(e.which)) { 
						showAutoCompleteArea(target);

						clearTimeout(autoCompleteId);

						autoCompleteId = setTimeout(function(){
							suggestAutoComplete(target, escapeSpecialCharacter(tagChunk), autoCompleteInput, allTags)
						},100);
					}

				});
			}
		}
	}

	var TagsNavigation = function(){
		return {
			forElement: function(element){
				pos = -3;
				element.keydown(function(e){
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
			}

		}

	}

	var TagsValidator = function(){
	var splitChar = TAGS_SPLITTER_CHAR;
	return {
		forElement : function(element, allTags){
			var allTagNames = $.makeArray(
				$(allTags).map(function(index, element){
					return element.name;
				})
			);

			var tagsNotFound = [];
			$.validator.addMethod(
					"only-existent-tags",
					function(value, element) {
						var tags = $($(element).val().split(splitChar));
						tagsNotFound = verifyIfExists(tags, tagsNotFound);
						var valid = tagsNotFound.length <= 0;
						return valid;
					},
					function(){
						return MESSAGES['use_only_existing_tags'] + "<b>" + tagsNotFound + "</b>";
					}
			);

			function verifyIfExists(tags, tagsNotFound){
				tagsNotFound = [];
				tags.each(function(index, tag){
					if(tag != "" && notContains(allTagNames, tag) && notContains(tagsNotFound, tag)){
						tagsNotFound.push(tag);
					}
			});
				return tagsNotFound;
			}

			function notContains(array, item){
				return array.indexOf(item) < 0;
			}

		}

	}

}

	var components = [new AutoCompleteDOM(), new TagsNavigation()];
	if (ANYONE_CAN_CREATE_TAGS == "true") {
		components.push(new TagsValidator());
	}
	new TagsManager(components);

}());
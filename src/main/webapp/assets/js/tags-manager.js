(function () {
	var element = $('#tags');
	var escapeMarkup = function (m) {
		return m;
	};
	var contains = function(val){
		var found = false;
		$.each(element.val().split(','), function(i,v){
			if(v == val){
				found =  true;
			}
		});
		return found;
	};



	element.select2({
		placeholder: "Enter a tag",
		minimumInputLength: 3,
		multiple: true,
		ajax: {
			dataType: 'json',
			quietMillis: 100,

			url: function (term, page) {
				return CONTEXT_PATH + "/question/searchTags/" + term;
			},
			results: function (data) {
				return {
					results: $.map(data, function (item) {
						if(!contains(item.name)) {
							return {
								text: item.name,
								slug: item.name,
								id: item.name,
								usage: item.usageCount,
								newVal: false
							};
						}
					})
				};
			}
		},
		createSearchChoice: function (term, data) {
			if ($(data).filter(function () {
				return this.text.localeCompare(term) === 0;
			}).length === 0) {
				return {
					text: '<div class="tag-new-list">' + term + ' <span class="tag-smalltext">(create new tag)</span></div>'
						+ '<hr style="margin-top:15px;"/>',
					slug: term,
					id: term,
					newVal: true
				};
			}
		},
		initSelection: function (element, callback) {
			var values = $(element).val();
			var v = $.map(values.split(','), function (item) {
				if (item != '') {
					return {id: item, text: item};
				} else {
					return null;
				}
			});
			callback(v);
		},
		escapeMarkup: escapeMarkup,
		formatResultCssClass: function (o) {
			if (o.newVal) {
				return "tag-new";
			} else {
				return "tag-found";
			}
		},
		formatResult: function (object, container, query) {
			if (object.newVal) {
				return $.fn.select2.defaults.formatResult(object, container, query, escapeMarkup);
			} else {
				return '<a class="tag-brutal">' + object.id + '</a> x ' + object.usage;
			}
		}
	}).on('select2-selecting', function (e) {
		var choice = e.choice;
		if (choice.newVal) {
			choice.text = choice.id + ' <b class="tag-new-star">*</b>'
		} else {
			choice.text = choice.id
		}

	});
}());
(function () {
	//replace the original textbox to allow for graceful javascript degredation
	var id = '#tags'
	$(id).replaceWith('<input multiple type="hidden" class="bigdrop" id="tags" name="tagNames" style="width:100%" value="' + $(id).val() + '"/>');

	var element = $(id);
	var escapeMarkup = function (m) {
		return m;
	};
	var contains = function (val) {
		var found = false;
		$.each(element.val().split(','), function (i, v) {
			if (v == val) {
				found = true;
			}
		});
		return found;
	};

	var ajaxResults = {};
	element.select2({
		placeholder: Messages.get("tagmanager.search.placeholder"),
		minimumInputLength: 3,
		multiple: true,
		ajax: {
			dataType: 'json',
			quietMillis: 100,

			url: function (term, page) {
				return CONTEXT_PATH + "/question/searchTags/" + term;
			},
			results: function (data) {
				ajaxResults = {};
				return {
					results: $.map(data, function (item) {
						ajaxResults[item.name.toLowerCase()] = true;
						if (!contains(item.name)) {
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
			var remoteSearch = $(data).filter(function () {
				return this.text.localeCompare(term) === 0;
			}).length;

			if (ajaxResults[term.toLowerCase()] && remoteSearch == 0) {
				return {};
			} else if (remoteSearch === 0) {
				return {
					text: '<div class="tag-new-list">' + term
						+ ' <span class="tag-smalltext">(' + Messages.get("tagmanager.create.new") + ')</span>'
						+ '</div><hr style="margin-top:15px;"/>',
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

})();
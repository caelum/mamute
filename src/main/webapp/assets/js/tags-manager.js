(function () {
	$('#tags').select2({
		placeholder: "Enter a tag",
		minimumInputLength: 3,
		multiple: true,
		ajax: {
			dataType: 'json',
			quietMillis: 100,

			url: function (term, page) {
				return CONTEXT_PATH+"/question/searchTags/" + term;
			},
			results: function (data) {
				return {
					results: $.map(data, function (item) {
						return {
							text: item.name,
							slug: item.name,
							id: item.name
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
					id: term,
					text: term + ' <b style="color:#21C542">*</b>',
					slug: term
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
		escapeMarkup: function (m) {
			return m;
		}
	});
}());
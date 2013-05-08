$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    email: "Este email não é válido",
	    equalTo: jQuery.validator.format("Os valores inseridos são diferentes."),
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	    maxlength: jQuery.validator.format("Por favor, insira no máximo {0} caracteres."),
	});
	
	$.validator.addMethod(
	    "date",
	    function(value, element) {
	    	if(value.length == 0){
	    		return true;	
	    	}
	        return value.match(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/(19[1-9][0-9]|20[01][0-3])$/);
	    },
	    "Esta data não é válida. Utilize uma data no formato dd/mm/yyyy"
	);
	
	var tagsNotFound = [];
	$.validator.addMethod(
			"only-existent-tags",
			function(value, element) {
				var tags = $($(element).val().split(" "));
				tagsNotFound = verifyIfExists(tags, tagsNotFound);
				var valid = tagsNotFound.length <= 0;
				return valid;
			},
			function(){
				return "Use apenas tags que existem! As seguintes tags não existem: <b>"+ tagsNotFound + "</b>";
			}
	);
	
	function verifyIfExists(tags, tagsNotFound){
		tagsNotFound = [];
		tags.each(function(index, tag){
			if(tag != "" && notContains(ALL_TAG_NAMES, tag) && notContains(tagsNotFound, tag)){
				tagsNotFound.push(tag);
			}
		});
		return tagsNotFound;
	}
	
	function notContains(array, item){
		return array.indexOf(item) < 0;
	}
	
	$.validator.addMethod(
			"brutal-url",
			function(value, element) {
				if(value.length == 0){
					return true
				}
				return value.match(/(www\.)?(.*\.)(.*)/);
			},
			"Insira uma url válida!"
	);
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
	
	$(".disable-on-submit").submit(function(e) {
		var form = $(this)
		if(form.valid()){
			var input = form.find("input[type='submit']");
			input.attr("disabled", "true");
			form.addClass("inactive");
		}
	});
	
});

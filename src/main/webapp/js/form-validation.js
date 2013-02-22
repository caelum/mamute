$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    email: "Este email não é válido",
	    url: "Insira uma url válida",
	    equalTo: jQuery.validator.format("O valor deste campo tem de ser igual ao do campo {0}."),
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	    maxlength: jQuery.validator.format("Por favor, insira no máximo {0} caracteres."),
	});
	
	$.validator.setDefaults({onkeyup : true});
	
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
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
});

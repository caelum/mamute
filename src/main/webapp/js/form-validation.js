$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    email: "Insira um email válido (exemplo@exemplo.com)",
	    url: "Insira uma url válida (http://...)",
	    date: "Insira uma data válida (dd/mm/yyyy)",
	    equalTo: jQuery.validator.format("O valor deste campo tem de ser igual ao do campo {0}."),
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	    maxlength: jQuery.validator.format("Por favor, insira no máximo {0} caracteres."),
	});
	
	$.validator.setDefaults({onkeyup : false});
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
});

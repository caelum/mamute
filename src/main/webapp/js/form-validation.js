$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    email: "Insira um email válido.",
	    equalTo: jQuery.validator.format("O valor deste campo tem de ser igual ao do campo {0}."),
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	});
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"}
			}
		});
	});
});

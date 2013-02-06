$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: "Este campo é necessário.",
	    minlength: jQuery.validator.format("Por favor, insira ao menos {0} caracteres."),
	});
	
	
	$(".validated-form").each(function(i,f){
		$(f).validate();
	});
});

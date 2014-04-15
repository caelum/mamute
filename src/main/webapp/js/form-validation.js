$(function(){
	jQuery.extend(jQuery.validator.messages, {
	    required: MESSAGES['not_empty'],
	    email: MESSAGES['invalid_email'],
	    equalTo: jQuery.validator.format(MESSAGES['not_equal']),
	    minlength: jQuery.validator.format(MESSAGES['minimum_characters']),
	    maxlength: jQuery.validator.format(MESSAGES['maximum_characters']),
	});
	
	$.validator.addMethod(
	    "date",
	    function(value, element) {
	    	if(value.length == 0){
	    		return true;	
	    	}
	        return value.match(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/(19[1-9][0-9]|20[01][0-3])$/);
	    },
	    MESSAGES['invalid_date']
	);
	

	$.validator.addMethod(
			"brutal-url",
			function(value, element) {
				if(value.length == 0){
					return true
				}
				return value.match(/(www\.)?(.*\.)(.*)/);
			},
			MESSAGES['invalid_url']
	);
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
});

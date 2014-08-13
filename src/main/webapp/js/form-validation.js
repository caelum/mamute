$(function(){
	$(document).on("messages-loaded",function(){

		jQuery.extend(jQuery.validator.messages, {
		    required: Messages.get('org.hibernate.validator.constraints.NotEmpty.message'),
		    email: Messages.get('org.hibernate.validator.constraints.Email.message'),
		    equalTo: jQuery.validator.format(Messages.get('validator.not.equal')),
		    minlength: jQuery.validator.format(Messages.get('validator.minimum.characters')),
		    maxlength: jQuery.validator.format(Messages.get('validator.maximum.characters')),
		});
		
	})

	$.validator.setDefaults({
		onkeyup: function(input){
			setTimeout(function(){
				$(input).valid();
			}, 2000)
		}
		
	})
	
	$.validator.addMethod(
	    "date",
	    function(value, element) {
	    	if(value.length == 0){
	    		return true;	
	    	}
	        return value.match(/^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/(19[1-9][0-9]|20[01][0-3])$/);
	    },
	    Messages.get('validator.invalid.date')
	);
	
	$.validator.addMethod(
			"brutal-url",
			function(value, element) {
				if(value.length == 0){
					return true
				}
				return value.match(/(www\.)?(.*\.)(.*)/);
			},
			Messages.get('validator.invalid.url')
	);

	
	$(".validated-form").each(function(){
		$(this).find("input").addClass("ignore");
	});
	
	$(".validated-form").each(function(i,f){
		$(f).validate({
			rules:{
				"passwordConfirmation":{equalTo:"#password"},
			}
		});
	});
});

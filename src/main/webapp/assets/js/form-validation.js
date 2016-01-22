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
		if(value.trim().length == 0){
			return true;
		}

		var dateFormat = $(element).data('dateformat');
		if(dateFormat.length == 0){
			return true;
		}

		var dayPattern = '(0[1-9]|[12][0-9]|3[01])';
		var monthPattern = '(0[1-9]|1[0-2])';
		var yearPattern = '(19[1-9][0-9]|20[012][0-9])';

		/* first escape characters which have a special meaning in regex (= make them literals): */
		var datePattern = '^' + dateFormat.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&") + '$';

		/* then convert the joda date format into regex: */
		datePattern = datePattern.replace(/dd/i, dayPattern);
		datePattern = datePattern.replace(/mm/i, monthPattern);
		datePattern = datePattern.replace(/yyyy/i, yearPattern);

		return value.match(new RegExp(datePattern));
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

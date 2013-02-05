var ForgotPassword = ForgotPassword || function(form) {
	this.form = form;
	this.error = "";
};

ForgotPassword.prototype.isPasswordValid = function() {
	var form = $(this.form);
	password = form.find("[name='password']").val();
	confirmation = form.find("[name='password_confirmation']").val();
	if(password != confirmation) {
		this.error = "forgot_password.doesnt_matches";
		return false;
	}
	return true;
}

ForgotPassword.prototype.validate = function() {
	if(!$(this.form).valid()) {
		return false;
	}
	return this.isPasswordValid();
}

$(".change-pass").on("submit", function(e) {
	var forgot = new ForgotPassword(this);
	if(!forgot.validate()) {
		e.preventDefault();
		console.log(forgot.error);
	}
});

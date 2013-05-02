<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img style="margin-bottom:30px;" src="http://guj.com.br/imgs/guj-mail-logo.png" />
	
	<p>	
		${localization.getMessage("notification_mail.welcome", [watcher.getName()])}
	</p>
	
	<p>
		${localization.getMessage("notification_mail.where", [linkerHelper.questionLink(emailAction.getQuestion()), emailAction.getQuestion().getTitle()])}
	</p>
	
	<p>
		${localization.getMessage("notification_mail.answer", [emailAction.getWhat().getAuthor().getName(), sanitizer.sanitize(emailAction.getWhat().getTrimmedContent())])}
	</p>
	
	<hr />
	<span style="display:block; color: #aaa;">
		${localization.getMessage("notification_mail.footer_message")}
	</span>
</body>
</html>


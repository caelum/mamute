<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img style="margin-bottom:30px;" src="http://guj.com.br/imgs/guj-mail-logo.png" />
	
	<p>	
		${localization.getMessage("notification_mail.welcome", [watcher.getName()])}
	</p>
	
	<p>
	<#assign answerId = emailAction.what.id?c >
	<#assign url = linkerHelper.questionLink(emailAction.getQuestion()) + "#answer-" + answerId>
		${localization.getMessage("notification_mail.where", [url, emailAction.getQuestion().getTitle()])}
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


<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img style="margin-bottom:30px;" src="${logoUrl}" />
	
	<p>	
		${localization.getMessage("notification_mail.welcome", [watcher.getName()])}
	</p>
	
	<p>
		<#assign answerId = emailAction.what.id?c >
		<#assign url = linkerHelper.mainThreadLink(emailAction.getMainThread()) + "#answer-" + answerId>
		${localization.getMessage("notification_mail.where", [url, emailAction.getMainThread().getTitle()])}
	</p>
	
	<p>
		${localization.getMessage("notification_mail.answer", [emailAction.getWhat().getAuthor().getName(), sanitizer.sanitize(emailAction.getWhat().getTrimmedContent())])}
	</p>
	
	<#if emailAction.getMainThread().hasAuthor(watcher)>
		<p>
			Caso a resposta tenha resolvido sua pergunta, não esqueça de marcá-la como correta. 
			Caso não tenha resolvido sua dúvida, edite sua pergunta ou adicione comentários para dar mais informações.
		</p>
	</#if>
	
	<hr />
	<span style="display:block; color: #aaa;">
		${localization.getMessage("notification_mail.footer_message")}
	</span>
</body>
</html>


<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img style="margin-bottom:30px;" src="${logoUrl}" />
	
	<p>	
		${bundle.getMessage("notification_mail.welcome", [watcher.getName()])}
	</p>
	
	<p>
		<#assign answerId = emailAction.what.id?c >
		<#assign url = linkerHelper.mainThreadLink(emailAction.getMainThread()) + "#answer-" + answerId>
		${bundle.getMessage("notification_mail.where", [url, emailAction.getMainThread().getTitle()])}
	</p>
	
	<p>
		${bundle.getMessage("notification_mail.answer", [emailAction.getWhat().getAuthor().getName(), sanitizer.sanitize(emailAction.getWhat().getTrimmedContent())])}
	</p>
	
	<#if emailAction.getMainThread().hasAuthor(watcher)>
		<p>
			${bundle.getMessage("notification_mail.mark_solution_and_comment_reminder")}
		</p>
	</#if>
	
	<hr />
	<span style="display:block; color: #aaa;">
		${bundle.getMessage("notification_mail.footer_message", [bundle.getMessage("site.name")])}
	</span>
</body>
</html>


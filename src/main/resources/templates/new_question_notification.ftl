<html>
	<body style="font-family:arial, sans-serif; font-size:12px;">
		<img style="margin-bottom:30px;" src="${logoUrl}" />

		<p>
			${bundle.getMessage("new_question_mail.greeting")}
		</p>
		<p>
			<#assign siteName = bundle.getMessage("site.name") >
			${bundle.getMessage("new_question_mail.message", [siteName])}
			${bundle.getMessage("new_question_mail.question_link", [questionLink, question.getTitle()])}
		</p>

		<span style="display:block; color: #aaa;">
			${bundle.getMessage("new_question_mail.footer", [siteName])}
		</span>
	</body>
</html>


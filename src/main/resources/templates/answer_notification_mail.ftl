<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img style="margin-bottom:30px;" src="http://guj.com.br/imgs/guj-mail-logo.png" />
	<div style="color:#222; font-size:14px; line-height:1.2em; padding:8px 0 8px 10px; background-color:#edf2f4;">
		${localization.getMessage("notifications_mail.table_title")}
	</div>
	<table cellspacing="0" style="font-family:arial, sans-serif; font-size:12px;width:100%;">
		<#list subscribablesDTO as subscribableDTO>
			<tr>
				<td style="padding: 5px 0; border: 1px solid #e6e6e6;">
					<span style="display:block; padding-top:11px;">${dateFormat.print(subscribableDTO.getSubscribable().getCreatedAt())} 
						<a href="${linkerHelper.questionLink(subscribableDTO.getQuestion())}" style="color: #4E82C2; text-decoration:none;">
							${subscribableDTO.question.title}
						</a>
					</span>
					<span style="color: #aaa">
						${localization.getMessage(subscribableDTO.getSubscribable().getTypeNameKey())}
					</span>
					${sanitizer.sanitize(subscribableDTO.subscribable.trimmedContent)}
				</td>
			</tr>
		</#list>
	</table>
	<hr />
	<span style="display:block; color: #aaa;">
		${localization.getMessage("notifications_mail.footer_message")}
	</span>
	GUJ
</body>
</html>


<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<div style="color:#222; font-size:14px; line-height:1.2em; padding:8px 0 8px 10px; background-color:#edf2f4;">Os seguintes itens que você acompanha no GUJ foram atualizados:</div>
	<table cellspacing="0" style="font-family:arial, sans-serif; font-size:12px;width:100%;">
		<#list subscribablesDTO as subscribableDTO>
			<tr>
				<td style="padding: 5px 0; border: 1px solid #e6e6e6;">
					<img style="float:left;" src="http://localhost:8080/images/icone.png" />
					<span style="display:block; padding-top:11px;">${dateFormat.print(subscribableDTO.getSubscribable().getCreatedAt())} 
						<a href="${linkerHelper.questionLink(subscribableDTO.getQuestion())}" style="color: #4E82C2; text-decoration:none;">
							${subscribableDTO.question.title}
						</a>
					</span>
					<span style="color: #aaa">
						${localization.getMessage(subscribableDTO.getSubscribable().getTypeNameKey())}
					</span>
					${subscribableDTO.subscribable.trimmedContent}
				</td>
			</tr>
		</#list>
	</table>
	<hr />
	GUJ
</body>
</html>


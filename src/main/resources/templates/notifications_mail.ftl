<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<div style="color:#222; font-size:14px; line-height:1.2em; padding:8px 0 8px 10px; background-color:#edf2f4;">Os seguintes itens que você acompanha no brutal foram atualizados:</div>
	<table cellspacing="0" style="font-family:arial, sans-serif; font-size:12px;width:100%;">
		<#list subscribables as subscribable>
			<tr>
				<td style="padding: 5px 0; border: 1px solid #e6e6e6;">
					<img style="float:left;" src="http://localhost:8080/images/icone.png" />
					<span style="display:block; padding-top:11px;">${dateFormat.print(subscribable.getCreatedAt())} 
						<a href="#" style="color: #4E82C2; text-decoration:none;">Accessibility issues</a>
					</span>
					<span style="color: #aaa">
						${subscribable.typeNameKey}
					</span>
					${subscribable.trimmedContent}
				</td>
			</tr>
		</#list>
	</table>
	<hr />
	Brutal
</body>
</html>


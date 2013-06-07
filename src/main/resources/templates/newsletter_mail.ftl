<#assign questionStyle = 'padding:10px; border-bottom: 1px dotted #ccc;'>
<#assign linkStyle = 'text-decoration: none; color: rgb(0, 119, 204);'>
<#assign titleStyle = 'margin: 15px 0; padding: 0 10px;'>
<#assign mainTitleStyle = 'margin: 0 0 0 20px; vertical-align: bottom; display: inline-block;'>

<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<table width="600" align="center">
		<tr>
	        <td style="border-bottom: 1px solid #222; padding-bottom: 10px">
	        	<img src="http://www.guj.com.br/imgs/guj-mail-logo.png">
	        	<h1 style="${mainTitleStyle}">Newsletter</h1>
        	</td>
		</tr>
	
		<tr>
			<td><h2 style="${titleStyle}">Questões principais dessa semana:</h2></td>
		</tr>
		
		<#list hotQuestions as question>
			<tr>
				<td style="${questionStyle}">
					<h3><a style="${linkStyle}" href="${linkToHelper.questionLink(question)}">${question.title}</a></h3>
					<p>${sanitizer.sanitize(question.getTrimmedContent())}</p>
				</td>
			</tr>
		</#list>
		
		<tr>
			<td><h2 style="${titleStyle}">Questões sem solução:</h2></td>
		</tr>
		<#list unansweredQuestions as question>
			<tr>
				<td style="${questionStyle}">
					<h3><a style="${linkStyle}" href="${linkToHelper.questionLink(question)}">${question.title}</a></h3>
					<p>${sanitizer.sanitize(question.getTrimmedContent())}</p>
				</td>
			</tr>
		</#list>
		
	</table>
</body>
</html>


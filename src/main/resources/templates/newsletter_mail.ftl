<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<img src="http://www.guj.com.br/imgs/guj-mail-logo.png">
	<h1>Newsletter - GUJ Respostas</h1>
	<table>
		<tr>
			<td><h2>Questões principais dessa semana:</h2></td>
		</tr>
		<#list hotQuestions as question>
			<tr>
				<td>
					${question.title}
					<p>${question.title}</p>
				</td>
				${question}
			</tr>
		</#list>
		<tr>
			<td><h2>Questões sem solução:</h2></td>
		</tr>
		<#list unansweredQuestions as question>
			<tr>
				<td>${question}</td>
			</tr>
		</#list>
	</table>
</body>
</html>


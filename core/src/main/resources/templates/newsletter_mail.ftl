<#assign questionStyle = 'padding:0px 10px; border-bottom: 1px dotted #ccc;'>
<#assign questionTitleStyle = 'margin: 3px 0 0'>
<#assign linkStyle = 'text-decoration: none; color: rgb(0, 119, 204);'>
<#assign titleStyle = 'margin: 15px 0; padding: 0 10px;'>
<#assign mainTitleStyle = 'margin: 10px; vertical-align: bottom; display: inline-block; font-size: 23px;'>
<#assign tagStyle = 'text-decoration: none; background-color: #f8f8f8; border: 1px solid #adc2d0; color: #3e6d8e; display: inline-block; font-size: .9em; padding: 0 .4em; line-height: 1.45em; border-radius: 5px;'>
<#assign userNameStyle = 'font-size: 0.8em; float: right;'>
<#assign userImgStyle = 'width: 18px; height: 18px;'>
<#assign paragraphStyle = 'overflow: hidden; margin: 7px 0;'>

<html>
<body style="font-family:arial, sans-serif; font-size:12px;">
	<table width="600" align="center">
		<tr>
	        <td style="border-bottom: 1px solid #222; padding-bottom: 10px">
	        	<img src="${logoUrl}">
	        	<h1 style="${mainTitleStyle}">${l10n.getMessage("newsletter_mail", [date, siteName, ""])}</h1>
        	</td>
		</tr>

		<tr>
			<td><h2 style="${titleStyle}">${l10n.getMessage("newsletter_mail.title.week_main_news")}</h2></td>
		</tr>
		
		<#list hotNews as news>
			<tr>
				<td style="${questionStyle}">
					<h3 style="${questionTitleStyle}"><a style="${linkStyle}" href="${linkToHelper.newsLink(news)}">${news.title}</a></h3>
					<p style="${paragraphStyle}">
						<span style="${userNameStyle}">${l10n.getMessage("newsletter_mail.created_by")} 
							<a style="${linkStyle}" href="${linkToHelper.userLink(news.author)}">${news.author.name}</a>
						</span>
					</p>
				</td>
			</tr>
		</#list>
	
		<tr>
			<td><h2 style="${titleStyle}">${l10n.getMessage("newsletter_mail.title.week_main_questions")}</h2></td>
		</tr>
		
		<#list hotQuestions as question>
			<tr>
				<td style="${questionStyle}">
					<h3 style="${questionTitleStyle}"><a style="${linkStyle}" href="${linkToHelper.mainThreadLink(question)}">${question.title}</a></h3>
					<p style="${paragraphStyle}">
	                    <#list question.getTags() as tag>
							<span><a style="${tagStyle}" href="${linkToHelper.tagLink(tag)}">${tag.name}</a></span>
						</#list>
						<span style="${userNameStyle}">${l10n.getMessage("newsletter_mail.asked_by")} <a style="${linkStyle}" href="${linkToHelper.userLink(question.author)}">${question.author.name}</a></span>
					</p>
				</td>
			</tr>
		</#list>
		<tr>
			<td><h2 style="${titleStyle}">${l10n.getMessage("newsletter_mail.title.unanswered_questions")}</h2></td>
		</tr>
		<#list unansweredQuestions as question>
			<tr>
				<td style="${questionStyle}">
					<h3 style="${questionTitleStyle}"><a style="${linkStyle}" href="${linkToHelper.mainThreadLink(question)}">${question.title}</a></h3>
					<p style="${paragraphStyle}">
	                    <#list question.getTags() as tag>
							<span><a style="${tagStyle}" href="${linkToHelper.tagLink(tag)}">${tag.name}</a></span>
						</#list>
						<span style="${userNameStyle}">${l10n.getMessage("newsletter_mail.asked_by")}  <a style="${linkStyle}" href="${linkToHelper.userLink(question.author)}">${question.author.name}</a></span>
					</p>
				</td>
			</tr>
		</#list>
		<tr>
			<td>
				<a href="http://www.caelum.com.br/" style="color: #666; font-size: 1.2em;text-decoration:none; margin-top: 2em; padding: 0px 10px 0px 10px;display: block;float: left;">${l10n.getMessage("newsletter.link.text.caelum")}</a>
				<a href="http://www.casadocodigo.com.br/" style="color: #666; font-size: 1.2em;text-decoration:none; margin-top: 2em; padding: 0px 10px 0px 10px;display: block;float: left;">${l10n.getMessage("newsletter.link.text.cdc")}</a>
				<a href="http://www.alura.com.br/" style="color: #666; font-size: 1.2em;text-decoration:none; margin-top: 2em; padding: 0px 10px 0px 10px;display: block;float: left;">${l10n.getMessage("newsletter.link.text.alura")}</a>
			</td>
		</tr>
		<tr>
			<td><a href="${unsubscribeLink}" style="display:block; color: #aaa; font-size: 10px;text-decoration:none; text-align: right; margin-top: 5em;">${l10n.getMessage("newsletter_mail.unsubscribe", [siteName])}</a></td>
		</tr>
	</table>
</body>
</html>


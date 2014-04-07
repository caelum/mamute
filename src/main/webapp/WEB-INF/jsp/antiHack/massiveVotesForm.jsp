<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.home.title" var="title"/>

<fmt:message key="metas.default.description" var="description">
	<fmt:param value="${siteName}" />
</fmt:message>

<fmt:message key="metas.generic.title" var="genericTitle">
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	<fmt:message key="antihack.show_votes"/>
</h2>

<form action="${linkTo[AntiHackController].showSuspects}" method="post">
	<label>Início</label>
	<input type="text" class="date datepicker text-input" name="begin">
	<label>Fim</label>
	<input type="text" class="date datepicker text-input" name="end">
	
	<label>Tipo de voto</label>
	<select name="voteType">
		<option value="UP"><fmt:message key="suspects.upvote"/></option>
		<option value="DOWN"><fmt:message key="suspects.downvote"/></option>
	</select>
	
	<input type="submit" value="<fmt:message key="antihack.show_votes"/>"/>
</form>
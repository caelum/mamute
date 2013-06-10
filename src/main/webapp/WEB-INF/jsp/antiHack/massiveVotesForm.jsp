<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	<fmt:message key="users.ranking"/>
</h2>

<form action="${linkTo[AntiHackController].showSuspects}" method="post">
	<input type="text" class="date datepicker" name="begin">
	<input type="text" class="date datepicker" name="end">
	
	<select name="voteType">
		<option value="UP"><fmt:message key="suspects.upvote"/></option>
		<option value="DOWN"><fmt:message key="suspects.downvote"/></option>
	</select>
	
	<input type="submit"/>
</form>
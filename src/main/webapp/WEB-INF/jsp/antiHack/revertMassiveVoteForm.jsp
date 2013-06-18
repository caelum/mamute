<fmt:message key="metas.home.title" var="title"/>
<fmt:message key="metas.default.description" var="description"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	<fmt:message key="antihack.revert"/>
</h2>

<form action="${linkTo[AntiHackController].revertMassiveVote}" method="post">

	<label for="karma">Reputação que deve ser revertida</label>
	<input type="text" class="text-input" name="karma">
	<label for="karma">Id do usuário</label>
	<input type="text" class="text-input" name="userId">
	
	<label for="karma">Reverter qual tipo de voto?</label>
	<select name="voteType">
		<option value="UP"><fmt:message key="suspects.upvote"/></option>
		<option value="DOWN"><fmt:message key="suspects.downvote"/></option>
	</select>
	
	<input type="submit" value="<fmt:message key="antihack.revert"/>"/>
</form>
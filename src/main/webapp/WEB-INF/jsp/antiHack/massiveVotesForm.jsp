<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.home.title']}"/>

<c:set var="description" value="${t['metas.default.description'].args(siteName)}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header facebookMetas="${true}" title="${genericTitle} - ${title}" description="${description}"/>

<h2 class="title page-title subheader">
	${t['antihack.show_votes']}
</h2>

<form action="${linkTo[AntiHackController].showSuspects}" method="post">
	<label>start</label>
	<tags:dateInput name="begin" id="massive-votes-begin" cssClass="datepicker" />
	<label>end</label>
	<tags:dateInput name="end" id="massive-votes-end" cssClass="datepicker" />

	<label>Tipo de voto</label>
	<select name="voteType">
		<option value="UP">${t['suspects.upvote']}</option>
		<option value="DOWN">${t['suspects.downvote']}</option>
	</select>
	
	<input type="submit" value="${t['antihack.show_votes']}"/>
</form>
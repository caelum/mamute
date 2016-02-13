<c:set var="title" value="${t['not_found.title']}"/>
<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title">${t['not_found.title']}</h2>
</div>

<div class="not-found">
	<p>${t['not_found.message']}:</p> 
	
	<ul>
		<li>${t['not_found.recent_questions'].args(linkTo[ListController].home)}</li>
		<li>${t['not_found.new_question'].args(linkTo[QuestionController].questionForm)}</li>
		<li>${t['not_found.search']}</li>
	</ul>
</div>
<div class="error-code">
	404
</div>

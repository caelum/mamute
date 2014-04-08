<fmt:message key="not_found.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<div class="subheader">
	<h2 class="title page-title"><fmt:message key="not_found.title"/></h2>
</div>

<div class="not-found">
	<p><fmt:message key="not_found.message"/>:</p> 
	
	<ul>
		<li><fmt:message key="not_found.recent_questions"><fmt:param value="${linkTo[ListController].home}"/></fmt:message></li>
		<li><fmt:message key="not_found.search"><fmt:param value="${linkTo[SearchController].search}"/></fmt:message></li>
		<li><fmt:message key="not_found.new_question"><fmt:param value="${linkTo[QuestionController].questionForm}"/></fmt:message></li>
	</ul>
</div>
<div class="error-code">
	404
</div>

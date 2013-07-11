<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<fmt:message key="metas.question.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<form class="validated-form question-form hinted-form" action='${linkTo[NewsController].newNews}' method="post" >
	<label for="news-title"><fmt:message key="news.title.label" /></label>
	<input id="news-title" type="text" class="required hintable text-input" 
		value="${question.title }" minlength="15" maxlength="150"
		name="title" placeholder="<fmt:message key="news.title.placeholder"/>" />
	
	<fmt:message var="descriptionPlaceholder" key="news.description.placeholder"/>
	<tags:markDown hintId="news-description-hint" htmlClass="required description-input" minlength="30"/>

	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.new_form.submit"/>" />
</form>

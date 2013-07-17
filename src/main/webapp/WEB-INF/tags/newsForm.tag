<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<form class="validated-form question-form hinted-form" action='${linkTo[NewsController].newNews}' method="post" >
	<div class="news-guideline guideline">
		<h2 class="title section-title hint-title"><fmt:message key="news.guideline.title" /></h2>
		<fmt:message key="news.guideline.body" />
	</div>
	<label for="news-title"><fmt:message key="news.title.label" /></label>
	<input id="news-title" type="text" class="required hintable text-input" 
		value="${question.title }" minlength="15" maxlength="150"
		name="title" placeholder="<fmt:message key="news.title.placeholder"/>" />
	
	<fmt:message var="descriptionPlaceholder" key="news.description.placeholder"/>
	<tags:markDown hintId="news-description-hint" htmlClass="required description-input" minlength="30"/>

	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.new_form.submit"/>" />
</form>
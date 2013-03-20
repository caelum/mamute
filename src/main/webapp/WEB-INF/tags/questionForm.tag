<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<form class="validated-form question-form hinted-form" action='${uri}' method="post" >
	<label for="question-title"><fmt:message key="question.title.label" /></label>
	<input id="question-title" type="text" class="required hintable text-input" value="${question.title }" data-hint-id="question-title-hint" minlength="15" maxlength="150" name="title">
	<tags:markDown value="${question.description}" hintId="question-description-hint" htmlClass="required" minlength="30"/>
	<label for="tags"><fmt:message key="question.tags.label"/></label>
	<ul class="tags autocompleted-tags complete-tags hidden" id="newquestion-tags-autocomplete"></ul>
	<input id="tags" type="text" autocomplete="off" name="tagNames" class="hintable autocomplete only-existent-tags text-input required" value="${question.tagsAsString }" data-hint-id="question-tags-hint" data-autocomplete-id="newquestion-tags-autocomplete"/>
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.new_form.submit"/>" />
</form>
<div class="form-hints">
	<div id="question-title-hint" class="hint">
		<fmt:message key="question.title.hint">
        	<fmt:param value="${linkTo[NavigationController].about}#faq"/>
		</fmt:message>
	</div>
	<div id="question-description-hint" class="hint"><fmt:message key="question.description.hint" /></div>
	<div id="question-tags-hint" class="hint"><fmt:message key="question.tags.hint" /></div>
</div>

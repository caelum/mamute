<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<form class="validated-form question-form hinted-form" action='<c:url value="${uri}"/>' method="post" >
	<label for="question-title"><fmt:message key="newquestion.title" /></label>
	<input id="question-title" type="text" class="required hintable text-input" value="${question.title }" data-hint-id="newquestion-title-hint" minlength="15" name="title">
	<tags:markDown value="${question.description}" hintId="newquestion-description-hint" />
	<label for="tags"><fmt:message key="newquestion.tags"/></label>
	<input id="tags" type="text" autocomplete="off" name="tagNames" class="hintable autocomplete  text-input" value="${question.tagsAsString }" data-hint-id="newquestion-tags-hint" data-autocomplete-id="newquestion-tags-autocomplete"/>
	<ul class="tags autocompleted-tags complete-tags hidden" id="newquestion-tags-autocomplete"></ul>
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="newquestion.submit"/>" />
</form>
<div class="form-hints">
	<div id="newquestion-title-hint" class="hint"><fmt:message key="title.hint" /></div>
	<div id="newquestion-description-hint" class="hint"><fmt:message key="description.hint" /></div>
	<div id="newquestion-tags-hint" class="hint"><fmt:message key="tags.hint" /></div>
</div>

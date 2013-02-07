<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@attribute
	name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<form class="validated-form question-form hinted-form" action='<c:url value="${uri}"/>' method="post" >
	<label for="question-title"><fmt:message key="newquestion.title" /></label>
	<input id="question-title" type="text" class="required hintable" value="${question.title }" data-hint-id="newquestion-title-hint" minlength="15" name="title">
	<div class="wmd">
		<div class="wmd-panel">
			<div id="wmd-button-bar"></div>
			<textarea class="required hintable wmd-input" id="wmd-input"
				data-hint-id="newquestion-description-hint" minlength="30"
				name="description">${question.description}</textarea>
		</div>
		<div id="wmd-preview" class="wmd-panel wmd-preview hidden"></div>
	</div>
	
	<label for="tags"><fmt:message key="newquestion.tags"/></label>
	<input id="tags" type="text" name="tagNames" class="hintable autocomplete" value="${question.tagsAsString }" data-hint-id="newquestion-tags-hint" data-autocomplete-id="newquestion-tags-autocomplete"/>
	<ul class="tags autocompleted-tags" id="newquestion-tags-autocomplete"></ul>
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="newquestion.submit"/>" />
</form>

<div id="newquestion-title-hint" class="hint"><fmt:message key="title.hint" /></div>
<div id="newquestion-description-hint" class="hint"><fmt:message key="description.hint" /></div>
<div id="newquestion-tags-hint" class="hint"><fmt:message key="tags.hint" /></div>

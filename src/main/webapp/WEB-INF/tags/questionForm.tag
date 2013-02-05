<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@attribute
	name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<form class="validated-form" action='<c:url value="${uri}"/>' method="post" >

	<label for="question-title"><fmt:message key="newquestion.title" /></label>
	<input id="question-title" type="text" class="required hintable" value="${question.title }" data-hint-id="newquestion-title-hint" minlength="15" name="title">
	<label for="wmd-input"><fmt:message key="newquestion.description" /></label>
	<div class="wmd">
		<div class="wmd-panel">
			<div id="wmd-button-bar"></div>
			<textarea class="required hintable wmd-input" id="wmd-input"
				data-hint-id="newquestion-description-hint" minlength="30"
				name="description">${question.description }</textarea>
		</div>
		<div id="wmd-preview" class="wmd-panel wmd-preview"></div>
	</div>
	<label for="tags"><fmt:message key="newquestion.tags"/></label>
	<input id="tags" type="text" name="tagNames" class="hintable autocomplete" value="${question.tagsAsString }" data-hint-id="newquestion-tags-hint" data-autocomplete-id="newquestion-tags-autocomplete"/>
	
	<input type="submit" />
	
	<dl id="newquestion-tags-autocomplete"></dl>
	<div id="newquestion-title-hint" class="hint"><fmt:message key="title.hint" /></div>
	<div id="newquestion-description-hint" class="hint"><fmt:message key="description.hint" /></div>
	<div id="newquestion-tags-hint" class="hint"><fmt:message key="tags.hint" /></div>

</form>

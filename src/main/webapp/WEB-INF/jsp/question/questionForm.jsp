<form class="validated-form" action='<c:url value="/question/ask"/>' method="post" >
	<label ><fmt:message key="newquestion.title" /><input id="question-title" type="text" class="required hintable" data-hint-id="newquestion-title-hint" minlength="15" name="question.title"></label>
	<label ><fmt:message key="newquestion.description" /><textarea class="required hintable" data-hint-id="newquestion-description-hint" minlength="30" name="question.description"></textarea></label>
	<input type="submit" />
	
	<div id="newquestion-title-hint" class="hint"><fmt:message key="newquestion.title.hint" /></div>
	<div id="newquestion-description-hint" class="hint"><fmt:message key="newquestion.description.hint" /></div>
</form>

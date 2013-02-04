<form class="validated-form" action='<c:url value="/question/ask"/>' method="post" >

	<label for="question-title"><fmt:message key="newquestion.title" /></label>
	<input id="question-title" type="text" class="required hintable" data-hint-id="newquestion-title-hint" minlength="15" name="question.title">
	<label for="wmd-input"><fmt:message key="newquestion.description" /></label>
	<div class="wmd">
		<div class="wmd-panel">
			<div id="wmd-button-bar"></div>
			<textarea class="required hintable wmd-input" id="wmd-input"
				data-hint-id="newquestion-description-hint" minlength="30"
				name="question.description"></textarea>
		</div>
		<div id="wmd-preview" class="wmd-panel wmd-preview"></div>
	</div>
	<label for="tags"><fmt:message key="newquestion.tags" /></label>
	<input id="tags" type="text" name="tagNames" class="hintable" data-hint-id="newquestion-tags-hint"/>
	
	<input type="submit" />
	
	<div id="newquestion-title-hint" class="hint"><fmt:message key="title.hint" /></div>
	<div id="newquestion-description-hint" class="hint"><fmt:message key="description.hint" /></div>
	<div id="newquestion-tags-hint" class="hint"><fmt:message key="tags.hint" /></div>

</form>

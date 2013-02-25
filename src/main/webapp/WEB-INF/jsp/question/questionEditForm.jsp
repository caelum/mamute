<h2 class="title page-title"><fmt:message key="question.edit_form.title"/></h2>

<form class="validated-form question-form hinted-form" action='<c:url value="/question/edit/${question.id}"/>' method="post" >
	<label for="question-title"><fmt:message key="newquestion.title" /></label>
	<input id="question-title" type="text" class="required hintable text-input" value="${question.title }" data-hint-id="newquestion-title-hint" minlength="15" name="title">
	<tags:markDown value="${question.description}" hintId="newquestion-description-hint" htmlClass="required" minlength="30"/>
	<label for="tags"><fmt:message key="newquestion.tags"/></label>
	<input id="tags" type="text" name="tagNames" autocomplete="off" class="hintable autocomplete  text-input" value="${question.tagsAsString }" data-hint-id="newquestion-tags-hint" data-autocomplete-id="newquestion-tags-autocomplete"/>
	<ul class="tags autocompleted-tags" id="newquestion-tags-autocomplete"></ul>
	
	<label for="comment"><fmt:message key="question.edit_form.comment" /></label>
	<input type="text" name="comment" class="hintable text-input" data-hint-id="newquestion-comment-hint" />
	
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="newquestion.submit"/>" />
</form>
<div class="form-hints">
	<div id="newquestion-title-hint" class="hint"><fmt:message key="title.hint" /></div>
	<div id="newquestion-description-hint" class="hint"><fmt:message key="description.hint" /></div>
	<div id="newquestion-tags-hint" class="hint"><fmt:message key="tags.hint" /></div>
	<div id="newquestion-comment-hint" class="hint"><fmt:message key="question.edit_form.comment.hint" /></div>
</div>
<h2 class="title page-title"><fmt:message key="question.edit_form.title"/></h2>

<form class="validated-form question-form hinted-form" action='<c:url value="/question/edit/${question.id}"/>' method="post" >
	<label for="question-title"><fmt:message key="question.title.label"/></label>
	<input id="question-title" type="text" class="required hintable text-input" value="${question.title }" data-hint-id="question-title-hint" minlength="15" name="title">
	<tags:markDown value="${question.description}" hintId="question-description-hint" htmlClass="required" minlength="30"/>
	<label for="tags"><fmt:message key="question.tags.label"/></label>
	<input id="tags" type="text" name="tagNames" autocomplete="off" class="hintable autocomplete  text-input" value="${question.tagsAsString }" data-hint-id="question-tags-hint" data-autocomplete-id="question-tags-autocomplete"/>
	<ul class="tags autocompleted-tags hidden" id="question-tags-autocomplete"></ul>
	
	<label for="comment"><fmt:message key="edit_form.comment.label" /></label>
	<input type="text" name="comment" class="hintable text-input" data-hint-id="question-comment-hint" />
	
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.edit_form.submit"/>" />
</form>
<div class="form-hints">
	<div id="question-title-hint" class="hint"><fmt:message key="question.title.hint" /></div>
	<div id="question-description-hint" class="hint"><fmt:message key="question.description.hint" /></div>
	<div id="question-tags-hint" class="hint"><fmt:message key="question.tags.hint" /></div>
	<div id="question-comment-hint" class="hint"><fmt:message key="edit_form.comment.hint" /></div>
</div>
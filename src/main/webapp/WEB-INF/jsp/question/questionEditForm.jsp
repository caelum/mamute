<fmt:message key="metas.question_edit.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title subheader page-title"><fmt:message key="question.edit_form.title"/></h2>
<form class="validated-form question-form hinted-form" action='${linkTo[QuestionController].edit(question,null,null,null,null)}' method="post" >
	<label for="question-title"><fmt:message key="question.title.label"/></label>
	<input id="question-title" type="text" class="required hintable text-input" value="<c:out value="${question.title}" escapeXml="true"/>" data-hint-id="question-title-hint" minlength="15" maxlength="150" name="title">
	<tags:markDown value="${question.description}" hintId="question-description-hint" htmlClass="required" minlength="30"/>
	<label for="tags"><fmt:message key="question.tags.label"/></label>
	<ul class="tags autocompleted-tags hidden" id="question-tags-autocomplete"></ul>
	
	<input id="tags" type="text" name="tagNames" autocomplete="off" 
		class="hintable autocomplete only-existent-tags text-input required" 
		value="${question.getTagsAsString(env.get('tags.splitter.char'))}" data-hint-id="question-tags-hint" 
		data-autocomplete-id="question-tags-autocomplete"/>
	
	<label for="comment"><fmt:message key="edit_form.comment.label" /></label>
	<input type="text" name="comment" 
		class="hintable text-input required" 
		minlength="5" data-hint-id="question-comment-hint" 
		value="${editComment}"
		placeholder="<fmt:message key="edit_form.comment.placeholder" />"/>
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.edit_form.submit"/>" />
</form>
<div class="form-hints">
	<div id="question-title-hint" class="hint"><fmt:message key="question.title.hint" /></div>
	<div id="question-description-hint" class="hint"><fmt:message key="question.description.hint" /></div>
	<div id="question-tags-hint" class="hint"><fmt:message key="question.tags.hint" /></div>
	<div id="question-comment-hint" class="hint"><fmt:message key="edit_form.comment.hint" /></div>
</div>
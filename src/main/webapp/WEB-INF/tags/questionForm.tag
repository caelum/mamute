<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="tagsRequired" value="${env.supports('feature.tags.mandatory') ? 'required' : ''}"/>

<form class="validated-form question-form form-with-upload  hinted-form" action='${uri}' method="post" autocomplete="off">
	<label for="question-title">${t['question.title.label']}</label>
	<input id="question-title" type="text" class="required hintable text-input question-title-input"
		   value="${question.title}" data-hint-id="question-title-hint" minlength="15" maxlength="150"
		   name="title" placeholder="${t['question.title.placeholder']}"/>

	<c:if test="${env.supports('feature.solr')}">
		<div id="question-suggestions" class="hidden">
			<h2 class="title section-title">${t['question.similars']}</h2>
			<ul class="suggested-questions-list"></ul>
		</div>
	</c:if>

	<c:set var="descriptionPlaceholder" value="${t['question.description.placeholder']}"/>
	<tags:markDown placeholder="${descriptionPlaceholder}" value="${question.description}"
				   hintId="question-description-hint" htmlClass="required description-input" minlength="30"/>

	<c:if test="${env.supports('feature.inhouse.upload')}">
		<tags:fileUploader/>
	</c:if>

	<label for="tags">${t['question.tags.label']}</label>
	<ul class="tags autocompleted-tags complete-tags hidden" id="newquestion-tags-autocomplete"></ul>

	<input id="tags" type="text" autocomplete="off" name="tagNames"
		   class="question-tags-input hintable autocomplete only-existent-tags text-input ${tagsRequired}"
		   value="${question.getTagsAsString(environment.get('tags.splitter.char'))}" data-hint-id="question-tags-hint"
		   data-autocomplete-id="newquestion-tags-autocomplete"/>

	<input class="post-submit big-submit" type="submit" value="${t['question.new_form.submit']}"/>

	<tags:checkbox-watch/>

</form>
<div class="form-hints">
	<div id="question-title-hint" class="hint">
		<c:set var="linkToFaq" value="${linkTo[NavigationController].about}'#faq'"/>
		${t['question.title.hint'].args(linkToFaq)}
	</div>
	<div id="question-description-hint" class="hint">${t['question.description.hint']}</div>
	<div id="question-tags-hint" class="hint">${t['question.tags.hint']}</div>
</div>
<ol class="${currentUser.current.karma <= 0 ? 'automatically-joyride' : ''}" id="intro">
	<tags:joyrideTip className="question-title-input" options="tipLocation:bottom" key="intro.new_question.title"/>
	<tags:joyrideTip className="description-input" options="tipLocation:bottom" key="intro.new_question.description"/>
	<tags:joyrideTip className="question-tags-input" options="tipLocation:bottom" key="intro.new_question.tags"/>
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about"/>
</ol>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="tagsRequired" value="${env.supports('feature.tags.mandatory') ? 'required' : ''}"/>

<form class="validated-form question-form hinted-form" action='${uri}' method="post" autocomplete="off">
	<label for="question-title"><fmt:message key="question.title.label"/></label>
	<input id="question-title" type="text" class="required hintable text-input question-title-input"
		   value="${question.title }" data-hint-id="question-title-hint" minlength="15" maxlength="150"
		   name="title" placeholder="<fmt:message key="question.title.placeholder"/>"/>

	<div id="question-suggestions">
		<h2 class="title section-title">Similar questions</h2>

		<div id="question-suggestion-items"></div>
	</div>

	<fmt:message var="descriptionPlaceholder" key="question.description.placeholder"/>
	<tags:markDown placeholder="${descriptionPlaceholder}" value="${question.description}"
				   hintId="question-description-hint" htmlClass="required description-input" minlength="30"/>

	<label for="tags"><fmt:message key="question.tags.label"/></label>
	<ul class="tags autocompleted-tags complete-tags hidden" id="newquestion-tags-autocomplete"></ul>

	<input id="tags" type="text" autocomplete="off"
		   name="tagNames"
		   class="question-tags-input hintable autocomplete only-existent-tags text-input ${tagsRequired}"
		   value="${question.getTagsAsString(environment.get('tags.splitter.char'))}" data-hint-id="question-tags-hint"
		   data-autocomplete-id="newquestion-tags-autocomplete"/>

	<input class="post-submit big-submit" type="submit" value="<fmt:message key="question.new_form.submit"/>"/>

	<tags:checkbox-watch/>

</form>
<div class="form-hints">
	<div id="question-title-hint" class="hint">
		<fmt:message key="question.title.hint">
			<fmt:param value="${linkTo[NavigationController].about}#faq"/>
		</fmt:message>
	</div>
	<div id="question-description-hint" class="hint"><fmt:message key="question.description.hint"/></div>
	<div id="question-tags-hint" class="hint"><fmt:message key="question.tags.hint"/></div>
</div>
<ol class="${currentUser.current.karma <= 0 ? 'automatically-joyride' : ''}" id="intro">
	<tags:joyrideTip className="question-title-input" options="tipLocation:bottom" key="intro.new_question.title"/>
	<tags:joyrideTip className="description-input" options="tipLocation:bottom" key="intro.new_question.description"/>
	<tags:joyrideTip className="question-tags-input" options="tipLocation:bottom" key="intro.new_question.tags"/>
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about"/>
</ol>

<script src="<c:url value="/js/deps/jquery.js"/>"></script>
<script type="text/javascript">
	function getQuestionSuggestions(data) {
		if (data.list == undefined || data.list.length == 0) {
			$("#question-suggestions").hide();
			return;
		}

		$("#question-suggestions").show();
		$("#question-suggestion-items").children().remove();
		$.each(data.list, function () {
			$("#question-suggestion-items").append(
							'<div class="question-suggestion-item">' +
							'<div class="info votes">' + this.voteCount + '<div class="subtitle">votes</div></div>' +
							'<div class="info answers">' + this.answerCount + '<div class="subtitle">answers</div></div>' +
							'<h3 class="title item-title main-thread-title question-title">' +
							'<a href="/' + this.id + '-' + this.information.sluggedTitle + '">' + this.information.title + '</a>' +
							'</h3>' +
							'</div>'
			);
		});
	}

	$("#question-title").keyup(function () {
		if ($("#question-title").val().length >= 5) {
			//keep from spamming the search endpoint on keypresses
			window.clearTimeout($(this).data("question-suggest-timeout"));
			$(this).data("question-suggest-timeout", setTimeout(function () {
				$.get("/searchAjax", {query: $("#question-title").val()}, getQuestionSuggestions);
			}, 1000));
		} else {
			$("#question-suggestions").hide();
		}
	});
</script>
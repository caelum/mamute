<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%><%@attribute
	name="uri" required="true" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<h2 class="title page-title"><fmt:message key="newanswer.answer.your_answer"/></h2>
<form action="${uri }" method="post" class="validated-form">
	<div class="wmd">
		<div class="wmd-panel">
			<div id="wmd-button-bar"></div>
			<textarea class="required  hintable wmd-input" id="wmd-input"
				minlength="30" name="description"
				data-hint-id="newanswer-answer-hint">${answer.description }</textarea>
		</div>
		<div id="wmd-preview" class="wmd-panel wmd-preview"></div>

	</div>

	<input class="newanswer-submit" value="<fmt:message key="newanswer.answer.submit"/>" type="submit" />

</form>

<div id="newanswer-answer-hint" class="hint">
	<fmt:message key="newanswer.answer.hint" />
</div>

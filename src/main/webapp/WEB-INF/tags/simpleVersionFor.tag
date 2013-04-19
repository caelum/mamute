<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="questionInformation" type="br.com.caelum.brutal.model.QuestionInformation" %>
<%@attribute name="answerInformation" type="br.com.caelum.brutal.model.AnswerInformation" %>
<c:if test="${not empty questionInformation}">
	<h2 class="title question-title"><tags:questionLinkFor question="${questionInformation.question}"/></h2>
	<div class="post-text">
		${questionInformation.markedDescription}
	</div>
</c:if>
<c:if test="${not empty answerInformation}">
	<h2 class="title question-title"><tags:questionLinkFor answer="${answerInformation.answer}"/></h2>
	<div class="post-text">
		${answerInformation.markedDescription}
	</div>
</c:if>
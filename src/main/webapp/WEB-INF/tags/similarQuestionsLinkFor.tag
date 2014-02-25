<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="question" type="org.mamute.model.Question" required="false" %>
<%@attribute name="answer" type="org.mamute.model.Answer" required="false" %>

<c:choose>
	<c:when test="${not empty question}">
		<a href="${linkTo[HistoryController].similarQuestions(question.id)}">
			<c:out value="${question.title}" escapeXml="true" />
		</a>
	</c:when>
	<c:when test="${not empty answer}">
		<a href="${linkTo[HistoryController].similarAnswers(answer.id)}">
			<c:out value="${answer.question.title}" escapeXml="true" />
		</a>
	</c:when>
</c:choose>


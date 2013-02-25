<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="false" %>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="false" %>

<c:choose>
	<c:when test="${empty question and not empty answer}">
		<a href="<c:url value="/questions/${answer.question.id}/${answer.question.sluggedTitle}"/>#answer-${answer.id}">
			${answer.question.title}
		</a>
	</c:when>
	<c:when test="${empty answer and not empty question}">
		<a href="<c:url value="/questions/${question.id}/${question.sluggedTitle}"/>">
			${question.title}
		</a>
	</c:when>
</c:choose>
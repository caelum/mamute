<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="false" %>

<c:choose>
	<c:when test="${not empty answer}">
		<a href="<c:url value="/questions/${question.id}/${question.sluggedTitle}"/>#answer-${answer.id}">
			${question.title}
		</a>
	</c:when>
	<c:otherwise>
		<a href="<c:url value="/questions/${question.id}/${question.sluggedTitle}"/>">
			${question.title}
		</a>
	</c:otherwise>
</c:choose>
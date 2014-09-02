<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="question" type="org.mamute.model.Question" required="false" %>
<%@attribute name="answer" type="org.mamute.model.Answer" required="false" %>

<c:choose>
	<c:when test="${empty question and not empty answer}">
		<a href="${linkTo[QuestionController].showQuestion(answer.question,answer.question.sluggedTitle)}#answer-${answer.id}">
			<c:out value="${answer.question.title}" escapeXml="${true}"/> 
		</a>
	</c:when>
	<c:when test="${empty answer and not empty question}">
		<a href="${linkTo[QuestionController].showQuestion(question,question.sluggedTitle)}">
			<c:out value="${question.title}" escapeXml="${true}"/> 
		</a>
	</c:when>
	<c:when test="${empty answer and empty question}">
		<span class="reverted">
			${t['massive.vote.reverted'].args(linkTo[NavigationController].about+'#perdi-meus-pontos')}
		</span>
	</c:when>
</c:choose>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>

<a href="${linkTo[QuestionController].showQuestion(answer.question,answer.question.sluggedTitle)}">
	<c:out value="${answer.question.title}" escapeXml="${true}"/>
</a>

<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="false" %>

<a href="<c:url value="/questions/${question.id }/${question.sluggedTitle}"/>${not empty answer ? '#'+answer.id : '' }">
	${question.title}
</a>
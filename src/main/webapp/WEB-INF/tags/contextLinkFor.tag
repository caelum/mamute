<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="context" type="org.mamute.model.ReputationEventContext" required="false" %>

<c:choose>
	<c:when test="${context.typeName == 'News'}">
		<a href="${linkTo[NewsController].showNews(context,context.sluggedTitle) }">
	</c:when>
	<c:otherwise>
		<a href="${linkTo[QuestionController].showQuestion(context,context.sluggedTitle) }">
	</c:otherwise>
</c:choose>
			<c:out value="${context.title}" escapeXml="${true}"/> 
		</a>
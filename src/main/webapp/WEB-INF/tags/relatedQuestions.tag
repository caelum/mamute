<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="questions" type="java.util.List" required="true" %>
<div class="subheader">
	<h3 class="title page-title">${t['questions.related']}</h3>
</div>
<ol class="related-questions">
	<c:forEach items="${questions}" var="question">
		<li><span>${question.voteCount}</span><tags:questionLinkFor question="${question}"/></li>
	</c:forEach>
</ol>
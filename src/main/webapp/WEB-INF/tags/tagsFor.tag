<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<ul class="tags">
	<c:forEach items="${question.tags}" var="tag">
		<li><tags:tag tag="${tag}"/></li>
	</c:forEach>
</ul>
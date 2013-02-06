<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<ul class="tags">
	<c:forEach items="${question.tags}" var="tag">
		<li><a href="${linkTo[ListController].withTag[tag.name]}" class="tag">${tag.name}</a></li>
	</c:forEach>
</ul>
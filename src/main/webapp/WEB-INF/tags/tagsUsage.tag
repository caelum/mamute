<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="tags" type="java.util.List" required="true" %>
<ol class="tags-usage">
	<c:forEach items="${tags}" var="tag">
		<li class="tags-item"><tags:tag tag="${tag}"/> x ${tag.usageCount}</li>
	</c:forEach>
</ol>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="tagsUsage" type="java.util.List" required="true" %>
<div class="subheader">
	<h3 class="title page-title">${t['tags.recent']}</h3>
</div>
<ol class="tags-usage">
	<c:forEach items="${tagsUsage}" var="tagUsage">
		<li class="tags-item"><tags:tag tag="${tagUsage.tag}"/> x ${tagUsage.usage}</li>
	</c:forEach>
</ol>
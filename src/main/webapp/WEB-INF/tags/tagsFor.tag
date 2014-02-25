<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="taggable" type="org.mamute.model.interfaces.Taggable" required="true" %>
<ul class="tags" itemprop="keywords">
	<c:forEach items="${taggable.tags}" var="tag">
		<li class="tags-item"><tags:tag tag="${tag}"/></li>
	</c:forEach>
</ul>
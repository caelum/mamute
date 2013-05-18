<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="totalPages" type="java.lang.Integer" required="true"%>
<%@attribute name="startingAt" type="java.lang.Integer" required="true"%>
<%@attribute name="delta" type="java.lang.Integer" required="true"%>
<%@attribute name="url" type="java.lang.String" required="true"%>
<%@attribute name="type" type="java.lang.String" required="false"%>
<%@attribute name="targetId" type="java.lang.String" required="false"%>

<ul class="pager">
	<c:if test="${startingAt - delta > 1 }">
		<li class="page-item ${(p == currentPage || p == 1) ? 'current' : ''}">
			<a rel="nofollow" href="${url}?p=1" data-type="${type}" data-target-id="${targetId}">1</a>
		</li>
		...
	</c:if>

	<c:forEach begin="${startingAt > delta ? startingAt - delta : startingAt}" end="${startingAt + delta}" var="p">
		<li class="page-item ${(p == currentPage || p == 1) ? 'current' : ''}">
			<a rel="nofollow" href="${url}?p=${p}" data-type="${type}" data-target-id="${targetId}">${p}</a>
		</li>
	</c:forEach>
	...
	<li class="page-item ${(p == currentPage || p == 1) ? 'current' : ''}">
		<a rel="nofollow" href="${url}?p=${totalPages}" data-type="${type}" data-target-id="${targetId}">${totalPages}</a>
	</li>
</ul>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="totalPages" type="java.lang.Integer" required="true"%>
<%@attribute name="url" type="java.lang.String" required="true"%>
<%@attribute name="type" type="java.lang.String" required="false"%>
<%@attribute name="targetId" type="java.lang.String" required="false"%>

<ul class="pager">
	<c:forEach begin="1" end="${totalPages}" var="p">
		<li class="page-item ${(p == currentPage || p == 1) ? 'current' : ''}">
			<a href="${url}?p=${p}" data-type="${type}" data-target-id="${targetId}">${p}</a>
		</li>
	</c:forEach>
</ul>
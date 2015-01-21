<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="totalPages" type="java.lang.Integer" required="true"%>
<%@attribute name="posts" type="java.util.List" required="true"%>
<%@attribute name="currentPage" type="java.lang.Integer" required="true"%>
<%@attribute name="type" type="java.lang.String" required="true"%>
<%@attribute name="order" type="java.lang.String" required="false"%>


<ul id="user-${type}" class="fixed-height">
	<c:forEach var="post" items="${posts}">
		<li class="ellipsis advanced-data-line">
			<span class="counter">${post.voteCount}</span>
			<c:choose>
				<c:when test="${type eq t['metas.answers_lowercase']}">
					<tags:questionLinkFor answer="${post}"/>
				</c:when>
				<c:otherwise>
					<tags:questionLinkFor question="${post}"/>
				</c:otherwise>
			</c:choose>
		</li>
	</c:forEach>
</ul>
<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2" type="${type}" targetId="user-${type}" order="${order}"/>
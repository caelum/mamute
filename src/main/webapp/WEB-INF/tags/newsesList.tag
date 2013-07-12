<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="newses" type="java.util.List" required="true"%>
<section class="news-content first-content">
	<h2 class="title page-title subheader"><fmt:message key="menu.newses"/></h2>
	<c:if test="${not empty newses}">
		<ol>
			<c:forEach var="news" items="${newses}">
				<tags:newsListItem news="${news}"/>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty newses}">
		<h2 class="title section-title"><fmt:message key="newses.empty_list" /></h2>
	</c:if>
	<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>
</section>
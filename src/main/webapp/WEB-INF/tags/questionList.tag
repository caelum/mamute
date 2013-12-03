<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="title" type="java.lang.String" required="true"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>
<%@attribute name="questions" type="java.util.List" required="true"%>
<%@attribute name="rssUrl" type="java.lang.String" required="false"%>
<%@attribute name="tag" type="br.com.caelum.brutal.model.Tag" required="false"%>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="tabs" type="java.util.List" required="false"%>

<tags:rssTagHeader unansweredTagLinks="${unansweredTagLinks}" tag="${tag}" rssUrl="${rssUrl}"
title ="${title}" showTabs="${not empty tabs}"/>
<c:if test="${not empty tag}">
	<tags:tagTabs tag="${tag}" hasAbout="${hasAbout}"/>
</c:if>

<c:if test="${not empty questions}">
	<ol class="question-list">
		<c:forEach var="question" items="${questions}">
			<tags:questionListItem question="${question}"/>
		</c:forEach>
	</ol>
</c:if>
<c:if test="${empty questions}">
	<h2 class="title section-title"><fmt:message key="questions.empty_list" /></h2>
</c:if>
<tags:pagination url="${currentUrl}" currentPage="${currentPage}" totalPages="${totalPages}" delta="2"/>


<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute type="org.mamute.model.Tag" name="tag" required="false"%>
<%@attribute type="java.lang.String" name="title" required="false"%>
<%@attribute name="unansweredTagLinks" type="java.lang.Boolean" required="false"%>
<%@attribute name="showTabs" type="java.lang.Boolean" required="false"%>

<c:if test="${empty unansweredTagLinks}">
	<c:set value="${false}" var="unansweredTagLinks" />
</c:if>

<div class="subheader">
	<c:if test="${unansweredTagLinks}">
		<tags:brutal-include value="headerTagsWithNoAnswer"/>
	</c:if>
	<c:if test="${not unansweredTagLinks}">
		<tags:brutal-include value="headerTags"/>
	</c:if>

	<c:if test="${not empty title}">
		<c:if test="${empty tabs}">
			<h2 class="title page-title">${title}</h2>
		</c:if>
	</c:if>
	<c:if test="${not empty tabs}">
		<tags:tabs title="${title}" useSubheader="${false}">
			<c:forEach var="tab" items="${tabs}">
				<c:set var="tabText" value="menu.top.${tab}"/>
				<a href="${linkTo[ListController].top(tab)}">${t[tabText]}</a>
			</c:forEach>
		</tags:tabs>
	</c:if>
</div>
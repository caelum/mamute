<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="list" type="java.util.List" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@attribute name="i18n" type="java.lang.String" required="true" %>
<%@attribute name="orderOptions" type="java.lang.Boolean" required="true" %>
<%@attribute name="withPagination" type="java.lang.Boolean" required="true" %>
<%@attribute name="count" type="java.lang.Long" required="false" %>
<%@attribute name="pages" type="java.lang.Long" required="false" %>


<section class="user-questions advanced-data-section">
	<div class="subheader">
		<h3 class="title section-title"><span class="counter">${count == null ? fn:length(list) : count}</span><tags:pluralize key="user_profile.${i18n}" count="${fn:length(list)}" /></h3>
		<c:if test="${orderOptions}">
			<tags:ordersFor type="${type}" user="${selectedUser}" targetId="user-${type}"/>
		</c:if>
	</div>
	<ul id="user-${type}" class="fixed-height">
	    <c:if test="${type == 'respostas'}">
			${answer.question.isVisibleFor(currentUser.current)}
			<c:forEach var="answer" items="${list}">
				<c:if test="${answer.question.isVisibleFor(currentUser.current)}">
					<li class="ellipsis advanced-data-line"><span class="counter">${answer.voteCount}</span> <tags:questionLinkFor answer="${answer}"/></li>
				</c:if>
			</c:forEach>
		</c:if>
		<c:if test="${type != 'respostas'}">
			${question.isVisibleFor(currentUser.current)}
			<c:forEach var="question" items="${list}">
				<c:if test="${question.isVisibleFor(currentUser.current)}">
					<li class="ellipsis advanced-data-line"><span class="counter">${question.voteCount}</span> <tags:questionLinkFor question="${question}"/></li>
				</c:if>
			</c:forEach>
		</c:if>
	</ul>
	<c:if test="${withPagination}">
	    <tags:pagination url="/usuario/${user.id}/${user.sluggedName}/${type}" type="${type}" targetId="user-${type}" totalPages="${pages}" delta="2" currentPage="1"/>
	</c:if>
</section>
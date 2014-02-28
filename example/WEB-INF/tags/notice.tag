<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="isLogged" type="java.lang.Boolean" required="true" %>
<%@attribute name="tags" type="java.util.List" required="true" %>

<c:set var="inlineTags">
	<c:forEach items="${tags}" var="tag">
		<tags:tag tag="${tag}"/>
	</c:forEach>
</c:set>

<section class="notice">
	<h3 class="title">
		<fmt:message key='${isLogged ? "notice.logged" : "notice.not_logged"}'>
			<fmt:param value="${inlineTags}" />
			<fmt:param value="${linkTo[QuestionController].newQuestion}" />
		</fmt:message>
	</h3>
</section>
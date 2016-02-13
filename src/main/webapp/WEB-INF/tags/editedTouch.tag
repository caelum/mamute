<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="touchable" type="org.mamute.model.interfaces.Touchable" required="true" %>
<%@attribute name="microdata" required="false" %>
<%@attribute name="showTime" type="java.lang.Boolean" required="false" %>
<%@attribute name="prettyFormat" type="java.lang.Boolean" required="false" %>

<c:if test="${empty showTime}">
	<c:set var="showTime" value="${true}" />
</c:if>

<c:if test="${empty prettyFormat}">
	<c:set var="prettyFormat" value="${true}" />
</c:if>

<tags:completeUser user="${touchable.information.author}" edited="true" microdata="${microdata}">
	<c:if test="${showTime}">
		<c:set var="nameClass" value="${touchable['class'].simpleName eq 'Question'}"/>
		<c:if test="${nameClass && editedLink}"> <a href="${linkTo[HistoryController].questionHistory(touchable.id)}"> </c:if>
			<time class="when" ${microdata ? 'itemprop="dateModified"' : ""} datetime="${touchable.information.createdAt}">${t['touch.edited']}
				<c:if test="${prettyFormat}">
					<tags:prettyTime time="${touchable.information.createdAt}"/>
				</c:if>
				<c:if test="${not prettyFormat}">
					<fmt:formatDate value="${touchable.information.createdAt.toGregorianCalendar().time}" pattern="MM/dd/yyyy"/>
				</c:if>
			</time>
		<c:if test="${nameClass && editedLink}"></a></c:if>
	</c:if>
</tags:completeUser>

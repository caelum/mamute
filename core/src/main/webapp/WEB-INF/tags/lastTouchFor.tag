<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="touchable" type="org.mamute.model.interfaces.Touchable" required="true" %>
<%@attribute name="showTime" type="java.lang.Boolean" required="false" %>
<%@attribute name="prettyFormat" type="java.lang.Boolean" required="false" %>

<c:if test="${empty showTime}">
	<c:set var="showTime" value="${true}" />
</c:if>
<c:if test="${empty prettyFormat}">
	<c:set var="prettyFormat" value="${true}" />
</c:if>

<div class="last-touch">
	<c:choose>
		<c:when test="${touchable.edited}">
			<tags:editedTouch touchable="${touchable}" showTime="${showTime}" prettyFormat="${prettyFormat}"/>		
		</c:when>
		<c:otherwise>
			<tags:createdTouch touchable="${touchable}" showTime="${showTime}"  prettyFormat="${prettyFormat}"/>
		</c:otherwise>
	</c:choose>
</div>
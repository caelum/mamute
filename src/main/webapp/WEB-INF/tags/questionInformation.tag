<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="count" type="java.lang.Integer" required="true" %>
<%@attribute name="key" type="java.lang.String" required="true" %>
<%@attribute name="information" type="java.lang.String" required="true" %>

<div class="info ${information}">${count}
	<c:choose>
		<c:when test="${count eq 1}">
			<span class="subtitle"><fmt:message key="${key}.singular"/></span>
		</c:when>
		<c:otherwise>
			<span class="subtitle"><fmt:message key="${key}.plural"/></span>
		</c:otherwise>
	</c:choose>
</div>
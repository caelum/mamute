<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="key" type="java.lang.String" required="true"%>
<%@attribute name="count" type="java.lang.Integer" required="true"%>

<c:choose>
	<c:when test="${count eq 1}">
		<fmt:message key="${key}.singular"/>
	</c:when>
	<c:otherwise>
		<fmt:message key="${key}.plural"/>
	</c:otherwise>
</c:choose>

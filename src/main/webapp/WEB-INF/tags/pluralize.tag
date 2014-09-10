<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="key" type="java.lang.String" required="true"%>
<%@attribute name="count" type="java.lang.Integer" required="true"%>

<c:choose>
	<c:when test="${count <= 1 && count >= -1}">
		<c:set var="key" value="${key}.singular"/>
	</c:when>
	<c:otherwise>
		<c:set var="key" value="${key}.plural"/>
	</c:otherwise>
</c:choose>

${t[key]}
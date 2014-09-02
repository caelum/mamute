<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="key" type="java.lang.String" required="true"%>
<%@attribute name="count" type="java.lang.Integer" required="true"%>

<c:choose>
	<c:when test="${count <= 1 && count >= -1}">
		${t['${key}.singular']}
	</c:when>
	<c:otherwise>
		${t['${key}.plural']}
	</c:otherwise>
</c:choose>

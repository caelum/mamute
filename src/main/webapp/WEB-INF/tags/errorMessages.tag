<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="errors" type="java.util.List" required="true" %>
<ul class="error-messages">
<c:if test="${not empty errors}">
	<c:forEach var="error" items="${errors}">
		<li class="error">${error.message}</li>
	</c:forEach>
</c:if>
</ul>
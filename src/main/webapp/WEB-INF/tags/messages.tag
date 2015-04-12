<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="messagesList" type="java.util.List" required="true" %>
<c:if test="${not empty messagesList}">
	<ul>
		<c:forEach var="message" items="${messagesList}">
			<li class="message ${message.category}">
			    ${message.message}
            </li>
		</c:forEach>
	</ul>
</c:if>
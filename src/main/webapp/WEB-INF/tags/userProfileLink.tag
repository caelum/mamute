<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="value" type="java.lang.String" required="false" %>
<%@attribute name="htmlClass" type="java.lang.String" required="false" %>

<a <c:if test="${not empty htmlClass}">class="${htmlClass}"</c:if> href="${linkTo[UserProfileController].showProfile[user.id][user.sluggedName]}">
	<c:choose>
		<c:when test="${not empty value}">
		     ${value}  
		</c:when>
		<c:otherwise>
		      ${user.name}
		</c:otherwise>
	</c:choose>
</a>

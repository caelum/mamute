<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="value" type="java.lang.String" required="false" %>
<%@attribute name="isPrivate" type="java.lang.Boolean" required="true" %>
<%@attribute name="htmlClass" type="java.lang.String" required="false" %>
<%@attribute name="before" type="java.lang.String" required="false" %>
<%@attribute name="after" type="java.lang.String" required="false" %>
<%@attribute name="microdata" required="false" %>

<a class="${htmlClass}" ${microdata ? 'itemprop="name"' : ''} href="${linkTo[UserProfileController].showProfile(user,user.sluggedName)}">
	${before}
	<c:choose>
		<c:when test="${not empty value}">
			<fmt:message key="${value}"/>
		</c:when>
		<c:when test="${isPrivate}">
			${not empty user.realName ? user.realName : user.name}
		</c:when>
		<c:otherwise>
			${user.name}
		</c:otherwise>
	</c:choose>
	${after}
</a>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="date" type="org.joda.time.DateTime" required="true" %>
<%@attribute name="touchText" type="java.lang.String" required="true" %>
<%@attribute name="itemProp" type="java.lang.String" required="false" %>

<div class="complete-user">
	<div class="when" itemprop="${itemProp}"><fmt:message key='${touchText}'/> <tags:prettyTime time="${date}"/></div>
	<img class="user-image" src="${user.smallPhoto}"/>
	<div class="user-info">
		<tags:userProfileLink user="${user}" htmlClass="user-name ellipsis" isPrivate="false"/>
		<div class="user-karma">${user.karma} 
		<c:choose>
			<c:when test="${user.karma == 1}"><fmt:message key="touch.karma.singular" /></c:when>
			<c:otherwise><fmt:message key="touch.karma.plural" /></c:otherwise>
		</c:choose>
		</div>
	</div>
</div>
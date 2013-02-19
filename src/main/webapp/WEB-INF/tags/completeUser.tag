<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="date" type="org.joda.time.DateTime" required="true" %>
<%@attribute name="touchText" type="java.lang.String" required="true" %>

<div class="complete-user">
	<div class="when"><fmt:message key='${touchText}'/> <tags:prettyTime time="${date}"/></div>
	<img class="user-image" src="${user.photo}?s=32"/>
	<div class="user-info">
		<tags:userProfileLink user="${user}" htmlClass="user-name ellipsis"></tags:userProfileLink>
		<div class="user-karma">${user.karma}</div>
	</div>
</div>
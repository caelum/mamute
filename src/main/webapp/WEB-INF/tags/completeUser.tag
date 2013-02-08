<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="date" type="org.joda.time.DateTime" required="true" %>
<div class="complete-user">
	<div class="when"><tags:prettyTime time="${date}"/></div>
	<img class="user-image" src="${user.photo}?s=32"/>
	<div class="user-info">
		<a class="user-name ellipsis" href="#">${user.name}</a>
		<div class="user-karma">${user.karma}</div>
	</div>
</div>
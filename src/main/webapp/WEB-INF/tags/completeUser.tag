<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<div class="complete-user">
	<jsp:doBody/>
	<img class="user-image" src="${not empty answers ? user.rankingPhoto : user.smallPhoto}"/>
	<div class="user-info">
		<tags:userProfileLink user="${user}" htmlClass="user-name ellipsis" isPrivate="false"/>
		<div title="<fmt:message key="touch.karma.title"/>" class="user-karma">${user.karma}<tags:pluralize key="touch.karma" count="${user.karma}" /></div>
	</div>
</div>
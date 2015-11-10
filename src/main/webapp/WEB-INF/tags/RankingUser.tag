<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="user" type="org.mamute.model.User" required="true" %>
<%@attribute name="isTagRanking" required="false" %>

<div class="ranking-user">
	<img class="user-image" src="${isTagRanking ? user.getSmallPhoto(env.get('gravatar.avatar.url')) : user.getMediumPhoto(env.get('gravatar.avatar.url'))}"/>
	<div class="user-info">
		<tags:userProfileLink user="${user}" htmlClass="user-name ellipsis"/>
		<div class="user-karma">${user.karma}<tags:pluralize key="touch.karma" count="${user.karma}" /></div>
	</div>
</div>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@attribute name="targetId" type="java.lang.String" required="true" %>
<%@attribute name="user" type="org.mamute.model.User" required="true" %>
<ul class="subheader-menu nav">
	<li class="nav-item">
		<a class="order-by selected" data-type="${type}" data-target-id="${targetId}" href="${linkTo[UserProfileController].typeByVotesWith(user.id, user.sluggedName, '', 1, type)}?order=ByVotes">${t['user_profile.order.votes']}</a>
	</li>
	<li class="nav-item">
		<a class="order-by" data-type="${type}" data-target-id="${targetId}" href="${linkTo[UserProfileController].typeByVotesWith(user.id, user.sluggedName, '', 1, type)}?order=ByDate">${t['user_profile.order.date']}</a>
	</li>
</ul>
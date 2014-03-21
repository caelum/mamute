<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="active" required="true" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<section class="basic-user-data user-data">
	<div class="subheader">
		<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title" isPrivate="true"/>
			<ul class="subheader-menu">
		<c:if test="${isCurrentUser}">
				<li><a href="${linkTo[UserProfileController].editProfile(selectedUser)}"><fmt:message key="user_profile.edit" /></a></li>
		</c:if>
				<c:if test="${currentUser.current.isModerator()}">
					<li>
						<a class="ban-user" href="#" data-url="${linkTo[UserProfileController].toogleBanned(selectedUser)}">
							<fmt:message key="${selectedUser.isBanned() ? 'user_profile.undo_ban' : 'user_profile.ban'}"/>
						</a>
					</li>
				</c:if>
			</ul>
	</div>
		
	<div class="image-and-information">
		<img class="user-image profile-image" src="${selectedUser.bigPhoto}"/>
		<span class="karma">${selectedUser.karma}</span>
		<span><fmt:message key="user_profile.reputation"/></span>
	</div>
	
	<ul class="data-list">
		<li class="data-line">
			<h5 class="data-section-title ellipsis"><fmt:message key="user_profile.bio" /></h5>
			<dl class="data-section">
				<dt class="data-title ellipsis"><fmt:message key="user_profile.name"/></dt>
				<dd class="data-description ellipsis">${selectedUser.name}</dd>
				<dt class="data-title ellipsis"><fmt:message key="user_profile.website"/></dt>
				<dd class="data-description ellipsis"><a rel='nofollow' href='<c:out value="${selectedUser.website}" escapeXml="true"/>'><c:out value="${selectedUser.website}" escapeXml="true"/></a></dd>
				<dt class="data-title ellipsis"><fmt:message key="user_profile.location"/></dt>
				<dd class="data-description ellipsis"><c:out value="${selectedUser.location}" escapeXml="true"/></dd>
				<dt class="data-title ellipsis"><fmt:message key="user_profile.age"/></dt>
				<dd class="data-description ellipsis">${selectedUser.age}</dd>
			</dl>
		</li>
		<li class="data-line">
			<h5 class="data-section-title ellipsis"><fmt:message key="user_profile.stats" /></h5>
			<dl class="data-section">
				<dt class="data-title ellipsis"><fmt:message key="user_profile.created_at"/></dt>
				<dd class="data-description ellipsis"><tags:prettyTime time="${selectedUser.createdAt}"/></dd>
			</dl>
		</li>
		<c:if test="${isCurrentUser or currentUser.moderator}">
		<li class="data-line">
			<h5 class="data-section-title ellipsis"><fmt:message key="user_profile.private" /></h5>
			<dl class="data-section">
				<dt class="data-title ellipsis"><fmt:message key="user_profile.email"/></dt>
				<dd class="data-description ellipsis profile-email">${selectedUser.email}</dd>
			</dl>
		</li>
		</c:if>
	</ul>
	
	<div class="about-me">
		<c:if test="${isCurrentUser && empty selectedUser.markedAbout}">
			<fmt:message key="user_profile.blank_about_me"/>
			<a href="${linkTo[UserProfileController].editProfile(selectedUser)}"><fmt:message key="user_profile.blank_about_me.click"/></a>
		</c:if>
		${selectedUser.markedAbout}
	</div>
</section>

<ul class="user-profile-tabs">
	<li class="tab ${active == 'summary' ? 'selected' : ''}">
		<a href='<c:url value="/usuario/${selectedUser.id}/${selectedUser.sluggedName}" />'>
			<fmt:message key="user_profile.summary"/>
		</a>
	</li>
	<li class="tab ${active == 'reputation' ? 'selected' : ''}">
		<a href='<c:url value="/usuario/${selectedUser.id}/${selectedUser.sluggedName}/reputacao" />'>
			<fmt:message key="user_profile.reputation"/>
		</a>
	</li>
</ul>

<jsp:doBody/>

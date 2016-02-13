<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="active" required="true" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<section class="basic-user-data user-data">
	<div class="subheader">
		<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title"/>
			<ul class="subheader-menu">
		<c:if test="${isCurrentUser}">
				<li><a href="${linkTo[UserProfileController].editProfile(selectedUser)}">${t['user_profile.edit']}</a></li>
				<c:if test="${env.supports('feature.logout_concealed')}">
					<li><a href="${linkTo[AuthController].logout}">${t['auth.logout_link']}</a></li>
				</c:if>

		</c:if>
				<c:set value="${selectedUser.id == currentUser.current.id}" var="sameUser"/>
				<c:if test="${currentUser.current.isModerator()}">
					<c:if test="${!sameUser}">
						<li>
							<a class="ban-user" title="${t['user_profile.ban.tootip']}" href="#" data-url="${linkTo[UserProfileController].toogleBanned(selectedUser)}">
								${t[selectedUser.isBanned() ? 'user_profile.undo_ban' : 'user_profile.ban']}
							</a>
						</li>
					</c:if>
					<c:if test="${env.supports('deletable.users') && !sameUser}">
						<li class="nav-item">
							<a href="#" title="${t['user_profile.delete.tootip']}" class="delete-user" data-item-type="user" data-delete-form="delete-user-form" data-confirm-deletion="true">
								${t['user.delete']}
							</a>
						</li>
						<form class="hidden delete-user-form" method="post" action="${linkTo[UserProfileController].delete(selectedUser.id)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
				</c:if>
			</ul>
	</div>
		
	<div class="image-and-information">
		<img class="user-image profile-image" src="${selectedUser.getBigPhoto(env.get('gravatar.avatar.url'))}"/>
		<span class="karma">${selectedUser.karma}</span>
		<span>${t['user_profile.reputation']}</span>
	</div>
	
	<ul class="data-list">
		<li class="data-line">
			<h5 class="data-section-title ellipsis">${t['user_profile.bio']}</h5>
			<dl class="data-section">
				<dt class="data-title ellipsis">${t['user_profile.name']}</dt>
				<dd class="data-description ellipsis">${selectedUser.name}</dd>
				<dt class="data-title ellipsis">${t['user_profile.website']}</dt>
				<dd class="data-description ellipsis"><a rel='nofollow' href='<c:out value="${selectedUser.website}" escapeXml="true"/>'><c:out value="${selectedUser.website}" escapeXml="true"/></a></dd>
				<dt class="data-title ellipsis">${t['user_profile.location']}</dt>
				<dd class="data-description ellipsis"><c:out value="${selectedUser.location}" escapeXml="true"/></dd>
				<dt class="data-title ellipsis">${t['user_profile.age']}</dt>
				<dd class="data-description ellipsis">${selectedUser.age}</dd>
			</dl>
		</li>
		<li class="data-line">
			<h5 class="data-section-title ellipsis">${t['user_profile.stats']}</h5>
			<dl class="data-section">
				<dt class="data-title ellipsis">${t['user_profile.created_at']}</dt>
				<dd class="data-description ellipsis"><tags:prettyTime time="${selectedUser.createdAt}"/></dd>
			</dl>
		</li>
		<c:if test="${isCurrentUser or currentUser.moderator}">
		<li class="data-line">
			<h5 class="data-section-title ellipsis">${t['user_profile.private']}</h5>
			<dl class="data-section">
				<dt class="data-title ellipsis">${t['user_profile.email']}</dt>
				<dd class="data-description ellipsis profile-email">${selectedUser.email}</dd>
			</dl>
		</li>
		</c:if>
	</ul>
	
	<div class="about-me">
		<c:if test="${isCurrentUser && empty selectedUser.markedAbout}">
			${t['user_profile.blank_about_me']}
			<a href="${linkTo[UserProfileController].editProfile(selectedUser)}">${t['user_profile.blank_about_me.click']}</a>
		</c:if>
		${selectedUser.markedAbout}
	</div>
</section>

<ul class="user-profile-tabs">
	<li class="tab ${active == 'summary' ? 'selected' : ''}">
		<a href='${linkTo[UserProfileController].showProfile(selectedUser, selectedUser.sluggedName)}'>
			${t['user_profile.summary']}
		</a>
	</li>
	<li class="tab ${active == 'reputation' ? 'selected' : ''}">
		<a href='${linkTo[UserProfileController].reputationHistory(selectedUser, selectedUser.sluggedName)}'>
			${t['user_profile.reputation']}
		</a>
	</li>
</ul>

<jsp:doBody/>

<div class="subheader">
	<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title" />
	<c:if test="${isCurrentUser}">
		<ul class="subheader-menu">
			<a href="${linkTo[UserProfileController].editProfile[selectedUser.id]}">edit</a>
		</ul>
	</c:if>
</div>
	
<div class="image-and-information">
	<img class="profile-image" src="${selectedUser.photo}?s=128"/>
	<span class="karma">${selectedUser.karma}</span>
	<span>reputation</span>
</div>

<ul class="data-list">
	<li class="data-line">
		<h5 class="data-section-title">bio</h5>
		<dl class="data-section">
			<dt class="data-title ellipsis"><fmt:message key="user_profile.website"/></dt>
			<dd class="data-description ellipsis">${selectedUser.website}</dd>
			<dt class="data-title ellipsis"><fmt:message key="user_profile.location"/></dt>
			<dd class="data-description ellipsis">${selectedUser.location}</dd>
			<c:if test="${isCurrentUser}">
				<dt class="data-title ellipsis"><fmt:message key="user_profile.email"/></dt>
				<dd class="data-description ellipsis">${selectedUser.email}</dd>
				<dt class="data-title ellipsis"><fmt:message key="user_profile.name"/></dt>
				<dd class="data-description ellipsis">${selectedUser.name}</dd>
			</c:if>
			<dt class="data-title ellipsis"><fmt:message key="user_profile.age"/></dt>
			<dd class="data-description ellipsis">${selectedUser.age}</dd>
		</dl>
	</li>
	<li class="data-line">
		<h5 class="data-section-title">stats</h5>
		<dl class="data-section">
			<dt class="data-title ellipsis"><fmt:message key="user_profile.created_at"/></dt>
			<dd class="data-description ellipsis"><tags:prettyTime time="${selectedUser.createdAt}"/></dd>
		</dl>
	</li>
</ul>

<div class="about-me">
	<c:if test="${isCurrentUser && empty selectedUser.markedAbout}">
		<fmt:message key="user_profile.blank_about_me"/>
		<a href="${linkTo[UserProfileController].editProfile[selectedUser.id]}">clique aqui para editar</a>
	</c:if>
	${selectedUser.markedAbout}
</div>






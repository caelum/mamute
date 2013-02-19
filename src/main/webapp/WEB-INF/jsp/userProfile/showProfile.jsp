<div class="subheader">
	<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title"></tags:userProfileLink>
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
	<li>
		<span class="data-title">bio</span>
		<dl>
			<span class="data-line ellipsis"><dt><fmt:message key="user_profile.website"/></dt><dd>${selectedUser.website}</dd></span>
			<span class="data-line ellipsis"><dt><fmt:message key="user_profile.location"/></dt><dd>${selectedUser.location}</dd></span>
			<c:if test="${isCurrentUser}">
				<span class="data-line ellipsis"><dt><fmt:message key="user_profile.email"/></dt><dd>${selectedUser.email}</dd></span>
				<span class="data-line ellipsis"><dt><fmt:message key="user_profile.name"/></dt><dd>${selectedUser.name}</dd></span>
			</c:if>
			<span class="data-line ellipsis"><dt><fmt:message key="user_profile.age"/></dt><dd>${selectedUser.age}</dd></span>
		</dl>
	</li>
	<li>
		<span class="data-title">stats</span>
		<dl>
			<span class="data-line"><dt><fmt:message key="user_profile.created_at"/></dt> <dd><tags:prettyTime time="${selectedUser.createdAt}"/></dd></span>
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

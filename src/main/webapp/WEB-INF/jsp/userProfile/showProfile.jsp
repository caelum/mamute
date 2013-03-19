<tags:header title="metas.profile.title" description="metas.default.description"/>

<section class="basic-user-data user-data">
	<div class="subheader">
		<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title" />
		<c:if test="${isCurrentUser}">
			<ul class="subheader-menu">
				<li><a href="${linkTo[UserProfileController].editProfile[selectedUser.id]}"><fmt:message key="user_profile.edit" /></a></li>
			</ul>
		</c:if>
	</div>
		
	<div class="image-and-information">
		<img class="profile-image" src="${selectedUser.mediumPhoto}"/>
		<span class="karma">${selectedUser.karma}</span>
		<span><fmt:message key="user_profile.reputation"/></span>
	</div>
	
	<ul class="data-list">
		<li class="data-line">
			<h5 class="data-section-title ellipsis"><fmt:message key="user_profile.bio" /></h5>
			<dl class="data-section">
				<dt class="data-title ellipsis"><fmt:message key="user_profile.website"/></dt>
				<dd class="data-description ellipsis"><a rel="nofollow" href="${selectedUser.website}">${selectedUser.website}</a></dd>
				<dt class="data-title ellipsis"><fmt:message key="user_profile.location"/></dt>
				<dd class="data-description ellipsis">${selectedUser.location}</dd>
				<c:if test="${isCurrentUser}">
					<dt class="data-title ellipsis"><fmt:message key="user_profile.email"/></dt>
					<dd class="data-description ellipsis profile-email">${selectedUser.email}</dd>
					<dt class="data-title ellipsis"><fmt:message key="user_profile.real_name"/></dt>
					<dd class="data-description ellipsis">${selectedUser.realName}</dd>
				</c:if>
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
	</ul>
	
	<div class="about-me">
		<c:if test="${isCurrentUser && empty selectedUser.markedAbout}">
			<fmt:message key="user_profile.blank_about_me"/>
			<a href="${linkTo[UserProfileController].editProfile[selectedUser.id]}"><fmt:message key="user_profile.blank_about_me.click"/></a>
		</c:if>
		${selectedUser.markedAbout}
	</div>
</section>
<section class="advanced-user-data user-data">
	<tags:userProfileAdvancedData list="${questionsByVotes}" type="questions" orderOptions="true">
		<c:forEach var="question" items="${questionsByVotes}">
			<li class="ellipsis advanced-data-line"><span class="counter">${question.voteCount}</span> <tags:questionLinkFor question="${question}"/></li>
		</c:forEach>
	</tags:userProfileAdvancedData>
	
	<tags:userProfileAdvancedData list="${answersByVotes}" type="answers" orderOptions="true">
		<c:forEach var="answer" items="${answersByVotes}">
			<li class="ellipsis advanced-data-line"><span class="counter">${answer.voteCount}</span> <tags:questionLinkFor answer="${answer}"/></li>
		</c:forEach>
	</tags:userProfileAdvancedData>
	
	<tags:userProfileAdvancedData list="${mainTags}" type="tags" orderOptions="false">
		<c:forEach var="tagUsage" items="${mainTags}">
			<li class="ellipsis advanced-data-line tag-line"><span class="counter tag-usage">${tagUsage.usage}</span> <tags:tag tag="${tagUsage.tag}"/></li>
		</c:forEach>
	</tags:userProfileAdvancedData>
</section>

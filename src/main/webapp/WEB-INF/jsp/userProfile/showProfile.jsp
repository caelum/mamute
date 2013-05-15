<fmt:message key="metas.profile.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<section class="basic-user-data user-data">
	<div class="subheader">
		<tags:userProfileLink user="${selectedUser}" htmlClass="title page-title" isPrivate="true"/>
		<c:if test="${isCurrentUser}">
			<ul class="subheader-menu">
				<li><a href="${linkTo[UserProfileController].editProfile[selectedUser]}"><fmt:message key="user_profile.edit" /></a></li>
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
				<dt class="data-title ellipsis"><fmt:message key="user_profile.real_name"/></dt>
				<dd class="data-description ellipsis">${selectedUser.realName}</dd>
			</dl>
		</li>
		</c:if>
	</ul>
	
	<div class="about-me">
		<c:if test="${isCurrentUser && empty selectedUser.markedAbout}">
			<fmt:message key="user_profile.blank_about_me"/>
			<a href="${linkTo[UserProfileController].editProfile[selectedUser]}"><fmt:message key="user_profile.blank_about_me.click"/></a>
		</c:if>
		${selectedUser.markedAbout}
	</div>
</section>
<section class="advanced-user-data user-data">
	<div class="advanced-data-line-wrapper">
		<tags:userProfileAdvancedData pages="${questionsPageTotal}" count="${questionsCount}" i18n="questions" list="${questionsByVotes}" type="perguntas" orderOptions="true" withPagination="true">
			<c:forEach var="question" items="${questionsByVotes}">
				<li class="ellipsis advanced-data-line"><span class="counter">${question.voteCount}</span> <tags:questionLinkFor question="${question}"/></li>
			</c:forEach>
		</tags:userProfileAdvancedData>
		
		<tags:userProfileAdvancedData pages="${answersPageTotal}" count="${answersCount}" i18n="answers" list="${answersByVotes}" type="respostas" orderOptions="true" withPagination="true">
			<c:forEach var="answer" items="${answersByVotes}">
				<li class="ellipsis advanced-data-line"><span class="counter">${answer.voteCount}</span> <tags:questionLinkFor answer="${answer}"/></li>
			</c:forEach>
		</tags:userProfileAdvancedData>
	</div>
	
	<div class="advanced-data-line-wrapper">
		<tags:userProfileAdvancedData i18n="tags" list="${mainTags}" type="tags" orderOptions="false" withPagination="false">
			<c:forEach var="tagUsage" items="${mainTags}">
				<li class="ellipsis advanced-data-line tag-line"><span class="counter tag-usage">${tagUsage.usage}</span> <tags:tag tag="${tagUsage.tag}"/></li>
			</c:forEach>
		</tags:userProfileAdvancedData>
		<tags:userProfileAdvancedData pages="${watchedQuestionsPageTotal}" count="${watchedQuestionsCount}" list="${watchedQuestions}" i18n="watched_questions" type="acompanhadas" orderOptions="false" withPagination="true">
			<c:forEach var="question" items="${watchedQuestions}">
				<li class="ellipsis advanced-data-line"><span class="counter">${question.voteCount}</span><tags:questionLinkFor question="${question}"/></li>
			</c:forEach>
		</tags:userProfileAdvancedData>
	</div>
</section>

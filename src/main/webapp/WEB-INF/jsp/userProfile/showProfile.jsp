<c:set var="title" value="${t['metas.profile.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab  active="summary">
	<section class="advanced-user-data user-data">
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedPostData pages="${questionsPageTotal}" count="${questionsCount}" i18n="questions" list="${questionsByVotes}" type="${t['metas.questions_lowercase']}" orderOptions="true" withPagination="true"/>
			<tags:userProfileAdvancedPostData pages="${answersPageTotal}" count="${answersCount}" i18n="answers" list="${answersByVotes}" type="${t['metas.answers_lowercase']}" orderOptions="true" withPagination="true"/>
		</div>
		
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedPostData pages="${watchedQuestionsPageTotal}" count="${watchedQuestionsCount}" list="${watchedQuestions}" i18n="watched_questions" type="${t['metas.watched_lowercase']}" orderOptions="false" withPagination="true"/>
			
			<tags:userProfileAdvancedData i18n="karma_history" list="${reputationHistory}" count="${selectedUser.karma}" type="historico-reputacao" orderOptions="false" withPagination="false">
				<c:forEach var="historyItem" items="${reputationHistory}">
					<tags:reputationHistoryItem historyItem="${historyItem}"/>
				</c:forEach>
				<a class="view-more" href="${linkTo[UserProfileController].reputationHistory(selectedUser, selectedUser.sluggedName)}">${t['show_more']}</a>
			</tags:userProfileAdvancedData>
		</div>
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedData i18n="tags" list="${userProfileMainTags}" type="tags" orderOptions="false" withPagination="false">
				<c:forEach var="tagUsage" items="${userProfileMainTags}">
					<li class="ellipsis advanced-data-line tag-line"><span class="counter tag-usage">${tagUsage.usage}</span> <tags:tag tag="${tagUsage.tag}"/></li>
				</c:forEach>
			</tags:userProfileAdvancedData>
		</div>
	</section>
</tags:userProfileTab>
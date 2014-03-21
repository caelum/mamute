<fmt:message key="metas.profile.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab  active="summary">
	<section class="advanced-user-data user-data">
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedPostData pages="${questionsPageTotal}" count="${questionsCount}" i18n="questions" list="${questionsByVotes}" type="perguntas" orderOptions="true" withPagination="true"/>
			<tags:userProfileAdvancedPostData pages="${answersPageTotal}" count="${answersCount}" i18n="answers" list="${answersByVotes}" type="respostas" orderOptions="true" withPagination="true"/>
		</div>
		
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedPostData pages="${watchedQuestionsPageTotal}" count="${watchedQuestionsCount}" list="${watchedQuestions}" i18n="watched_questions" type="acompanhadas" orderOptions="false" withPagination="true"/>
			
			<tags:userProfileAdvancedData i18n="karma_history" list="${reputationHistory}" count="${selectedUser.karma}" type="historico-reputacao" orderOptions="false" withPagination="false">
				<c:forEach var="historyItem" items="${reputationHistory}">
					<tags:reputationHistoryItem historyItem="${historyItem}"/>
				</c:forEach>
				<a class="view-more" href="<c:url value='/usuario/${selectedUser.id}/${selectedUser.sluggedName}/reputacao' />"><fmt:message key="show_more" /></a>
			</tags:userProfileAdvancedData>
		</div>
		<div class="advanced-data-line-wrapper">
			<tags:userProfileAdvancedData i18n="tags" list="${mainTags}" type="tags" orderOptions="false" withPagination="false">
				<c:forEach var="tagUsage" items="${mainTags}">
					<li class="ellipsis advanced-data-line tag-line"><span class="counter tag-usage">${tagUsage.usage}</span> <tags:tag tag="${tagUsage.tag}"/></li>
				</c:forEach>
			</tags:userProfileAdvancedData>
		</div>
	</section>
</tags:userProfileTab>
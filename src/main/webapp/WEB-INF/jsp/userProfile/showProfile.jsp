<fmt:message key="metas.profile.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab  active="summary">
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
			<tags:userProfileAdvancedData pages="${watchedQuestionsPageTotal}" count="${watchedQuestionsCount}" list="${watchedQuestions}" i18n="watched_questions" type="acompanhadas" orderOptions="false" withPagination="true">
				<c:forEach var="question" items="${watchedQuestions}">
					<li class="ellipsis advanced-data-line"><span class="counter">${question.voteCount}</span><tags:questionLinkFor question="${question}"/></li>
				</c:forEach>
			</tags:userProfileAdvancedData>
			<tags:userProfileAdvancedData i18n="karma_history" list="${reputationHistory}" count="${selectedUser.karma}" type="historico-reputacao" orderOptions="false" withPagination="false">
				<c:forEach var="historyItem" items="${reputationHistory}">
					<li class="ellipsis advanced-data-line">
						<c:set var="reputationClass" value="${historyItem.karma > 0 ? 'positive-karma' : historyItem.karma < 0 ? 'negative-karma' : 'neutral-karma'}" />
						<span class="reputation-won">
							<span class="counter karma-value ${reputationClass}">${historyItem.karma > 0 ? '+' : ''}${historyItem.karma}</span>
						</span>
						<tags:questionLinkFor question="${historyItem.question}"/></li>
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
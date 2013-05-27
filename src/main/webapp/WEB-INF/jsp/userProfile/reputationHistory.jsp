<fmt:message key="metas.profile.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab active="reputation">
<h2 class="title page-title"><span class="grey-text">${selectedUser.karma}</span> <span class="highlight"><fmt:message key="user_profile.reputation" /></span></h2>
<section class="advanced-user-data user-data">
	<ul class="karma-history">
		<c:forEach var="historyItem" items="${reputationHistory}">
			<li>
			<c:set var="reputationClass" value="${historyItem.karma > 0 ? 'positive-karma' : 'negative-karma'}" /> 
				<span class="reputation-won">
					<span class="counter karma-value ${reputationClass}">${historyItem.karma > 0 ? '+' : ''}${historyItem.karma}</span>
				</span>
				<span class="event-time"><tags:prettyTime time="${historyItem.date}"/></span>
				<span class="question-link"><tags:questionLinkFor question="${historyItem.question}"/></span>
			</li>
		</c:forEach>
	</ul>
</section>
</tags:userProfileTab>
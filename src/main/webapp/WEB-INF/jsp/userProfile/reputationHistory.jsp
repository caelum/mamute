<fmt:message key="metas.profile.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab active="reputation">
<section class="advanced-user-data user-data">
	<c:forEach var="historyItem" items="${reputationHistory}">
		<li class="ellipsis advanced-data-line">
			<span class="${historyItem.karma > 0 ? 'positive-karma' : 'negative-karma'}">
				${historyItem.karma > 0 ? '+' : ''} ${historyItem.karma}
			</span>
			<tags:prettyTime time="${historyItem.date}" />
			<tags:questionLinkFor question="${historyItem.question}"/>
		</li>
	</c:forEach>
</section>
</tags:userProfileTab>
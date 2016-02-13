<c:set var="title" value="${t['metas.profile.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:userProfileTab active="reputation">
<h2 class="title page-title"><span class="grey-text">${selectedUser.karma}</span> <span class="highlight">${t['user_profile.reputation']}</span></h2>
<section class="advanced-user-data user-data">
	<ul class="karma-history">
		<c:forEach var="historyItem" items="${reputationHistory}">
			<tags:reputationHistoryItem historyItem="${historyItem}">
				<span class="event-time"><tags:prettyTime time="${historyItem.date}"/></span>
			</tags:reputationHistoryItem>
		</c:forEach>
	</ul>
</section>
</tags:userProfileTab>
<fmt:message key="metas.unmoderated.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated['pergunta']}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated['resposta']}"><fmt:message key="moderation.answers"/></a>
	<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
</tags:tabs>

<ul class="pending-questions">
	<c:forEach var="entry" items="${pending.entrySet}">
		<li class="pending">
			<c:if test="${type eq 'pergunta'}">
				<h3 class="title item-title">
					<a href="${linkTo[HistoryController].similarQuestions[entry.key.id]}">${entry.key.title}</a>
				</h3>
				<tags:tagsFor taggable="${entry.key}"></tags:tagsFor>
			</c:if>
			<c:if test="${type eq 'resposta'}">
				<h3 class="title item-title">
					<a href="${linkTo[HistoryController].similarAnswers[entry.key.id]}">${entry.key.question.title}</a>
				</h3>
				<tags:tagsFor taggable="${entry.key.question}"></tags:tagsFor>
			</c:if>
			
			<div class="stats">
				<c:set var="information" value="${entry.value[fn:length(entry.value)-1]}"/>
				<span class="last-updated-at"><tags:prettyTime time="${information.createdAt}" /></span>
				<tags:userProfileLink user="${information.author}"></tags:userProfileLink>
			</div>
		</li>
	</c:forEach>
</ul>
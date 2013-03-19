<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<tags:header title="metas.unmoderated.title" description="metas.default.description"/>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated['question']}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated['answer']}"><fmt:message key="moderation.answers"/></a>
	<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
</tags:tabs>

<ul class="pending-questions">
	<c:forEach var="entry" items="${pending.entrySet}">
		<li class="pending">
			<c:if test="${type eq 'question'}">
				<h3 class="title item-title">
					<a href="${linkTo[HistoryController].similarQuestions[entry.key.id]}">${entry.key.title}</a>
				</h3>
				<tags:tagsFor taggable="${entry.key}"></tags:tagsFor>
			</c:if>
			<c:if test="${type eq 'answer'}">
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
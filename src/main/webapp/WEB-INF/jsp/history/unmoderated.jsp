<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="subheader subheader-with-tab">
	<h2 class="title page-title"><fmt:message key="moderation"/></h2>
	<div class="tabs">
		<a href="${linkTo[HistoryController].unmoderated['Question']}"><fmt:message key="moderation.questions"/></a>
		<a href="${linkTo[HistoryController].unmoderated['Answer']}"><fmt:message key="moderation.answers"/></a>
	</div>
</div>
<ul class="pending-questions">
	<c:forEach var="entry" items="${pending.entrySet}">
		<li class="pending">
			<c:if test="${type eq 'Question'}">
				<h3 class="title item-title">
					<a href="${linkTo[HistoryController].similarQuestions[entry.key.id]}">${entry.key.title}</a>
				</h3>
				<tags:tagsFor taggable="${entry.key}"></tags:tagsFor>
			</c:if>
			<c:if test="${type eq 'Answer'}">
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
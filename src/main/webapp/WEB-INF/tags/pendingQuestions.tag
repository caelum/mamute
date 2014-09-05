<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="history" type="org.mamute.model.ModeratableAndPendingHistory" required="true" %>

<c:if test="${empty history.entrySet}">
	<h2 class="title section-title">${t['moderation.edit.questions.empty']}</h2>
</c:if>

<c:if test="${not empty history.entrySet}">
	<h1 class="flagged-item-title-moderator">
		${t['menu.questions']}
	</h1>

	<ul>
		<c:forEach var="entry" items="${history.entrySet}">
			<c:set var="question" value="${entry.key}"/>
			<li class="post-item question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
				
				<div class="post-information question-information">
					<tags:postItemInformation key="post.list.vote"
						count="${question.voteCount}" information="votes"
						htmlClass="question-info" />
					<tags:postItemInformation key="question.list.answer"
						count="${question.answersCount}"
						information="answers ${question.solved ? 'solved' : ''} ${question.answersCount >= 1 ? 'answered' : ''}"
						htmlClass="question-info" />
				</div>
				
				<div class="summary question-summary">
					<div class="item-title-wrapper">
						${t['moderation.edits'].args(entry.value.size())}
						<h3 class="moderator-title item-title main-thread-title question-title">
							<tags:similarQuestionsLinkFor question="${entry.key}"/>
						</h3>
						<tags:tagsFor taggable="${question}" />
						<div class="post-simple-information">
							${question.views}
							<tags:pluralize key="post.list.view" count="${question.views}" />
						</div>
					</div>
					<tags:lastTouchFor touchable="${question}" />
				</div>
				
				<div
					class="${question.hasInteraction(currentUser.current) ? 'interaction' : ''}"
					title="${t['user.interactions']}">
				</div>
			</li>
		</c:forEach>
	</ul>
</c:if>

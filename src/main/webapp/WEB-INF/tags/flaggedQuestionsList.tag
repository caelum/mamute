<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="list" type="java.util.List" required="true" %>

<c:if test="${empty list}">
	<h2 class="title section-title">${t['moderation.flagged.questions.empty']}</h2>
</c:if>

<c:if test="${not empty list}">
	<h1 class="flagged-item-title-moderator">
			${t['menu.questions']}
	</h1>

	<ul>
		<c:forEach var="flaggableQuestion" items="${list}">
		<c:set var="question" value="${flaggableQuestion.flaggable}"/>
			<li class="post-item flagged-moderator-item question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
				
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
					<h5>
						${t['moderation.flags'].args(question.flags.size())}
					</h5>
						<h3 class="moderator-title item-title main-thread-title question-title">
							<tags:questionLinkFor question="${question}" />
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
				
				<div
					class="${question.flags.size() >= 5 ? 'heavy-flagged' : ''}"
					title="${t['moderation.flagged.lots']}">
				</div>
			</li>
		</c:forEach>
	</ul>
</c:if>

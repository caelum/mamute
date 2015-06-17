<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="list" type="java.util.List" required="true" %>
<%@attribute name="links" type="java.util.List" required="false" %>

<c:if test="${empty list}">
	<h2 class="title section-title">${t['moderation.flagged.comments.empty']}</h2>
</c:if>

<c:if test="${not empty list}">
	<h1 class="flagged-item-title-moderator">
			${t['menu.comments']}
	</h1>
</c:if>

<ul>
	<c:forEach var="flaggableComment" items="${list}" varStatus="i">

		<c:set var="comment" value="${flaggableComment.flaggable}"/>
		<c:set var="question" value="${links.get(i.count-1)}"/>

		<li class="post-item flagged-moderator-item question-item ${comment.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">

			<div class="post-information question-information">
				<tags:postItemInformation key="post.list.vote"
					count="${comment.voteCount}" information="votes"
					htmlClass="question-info" />
			</div>

			<div class="summary question-summary">
				<div class="item-title-wrapper">
					${t['moderation.flags'].args(comment.flags.size())}
					<h3 class="moderator-title item-title main-thread-title question-title">
						<tags:questionLinkFor question="${question}" />
					</h3>
					<p class="ellipsis">${comment.comment}</p>
					<tags:tagsFor taggable="${question}" />
					<div class="post-simple-information">
						${question.views}
						<tags:pluralize key="post.list.view" count="${question.views}" />
					</div>

				</div>
				<div>
					<tags:userProfileLink user="${comment.author}" htmlClass="user-name ellipsis"/>
				</div>
			</div>

			<div
				class="${question.hasInteraction(currentUser.current) ? 'interaction' : ''}"
				title="${t['user.interactions']}">
			</div>

			<div
				class="${comment.flags.size() >= 5 ? 'heavy-flagged' : ''}"
				title="${t['moderation.flagged.lots']}">
			</div>
		</li>
	</c:forEach>
</ul>
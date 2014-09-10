<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="history" type="org.mamute.model.ModeratableAndPendingHistory" required="true" %>

<c:if test="${empty history.entrySet}">
	<h2 class="title section-title">${t['moderation.edit.answers.empty']}</h2>
</c:if>

<c:if test="${not empty history.entrySet}">
	<h1 class="flagged-item-title-moderator">
		${t['menu.answers']}
	</h1>
	
	<ul>
		
		<c:forEach var="entry" items="${history.entrySet}">
			<c:set var="answer" value="${entry.key}"/>
			<c:set var="question" value="${answer.question}"/>
			<li class="post-item question-item ${answer.question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
				
				<div class="post-information question-information">
					<tags:postItemInformation key="post.list.vote"
						count="${answer.voteCount}" information="votes"
						htmlClass="question-info" />
				</div>
				
				<div class="summary question-summary">
					<div class="item-title-wrapper">
						${t['moderation.edits'].args(entry.value.size())}
						<h3 class="moderator-title item-title main-thread-title question-title">
							<tags:similarQuestionsLinkFor answer="${answer}"/>
						</h3>
						<tags:tagsFor taggable="${question}" />
						<div class="post-simple-information">
							${question.views}
							<tags:pluralize key="post.list.view" count="${question.views}" />
						</div>
					</div>
					<tags:lastTouchFor touchable="${answer}" />
				</div>
			</li>
		</c:forEach>
	</ul>	
</c:if>
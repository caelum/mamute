<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="list" type="java.util.List" required="true" %>
<ul>
	<h1 class="flagged-item-title-moderator">Respostas</h1>
	<c:forEach var="flaggableAnswer" items="${list}">
	<c:set var="answer" value="${flaggableAnswer.flaggable}"/>
		<li class="post-item question-item ${answer.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
			<div class="post-information question-information">
				<tags:postItemInformation key="post.list.vote"
					count="${answer.voteCount}" information="votes"	
					htmlClass="question-info" />
			</div>
			
			<div class="summary question-summary">
				<div class="item-title-wrapper">
				<h5>Flags: ${answer.flags.size()}</h5>
					<h3 class="title item-title main-thread-title question-title">
						<tags:questionLinkFor answer="${answer}" />
					</h3>
					<p class="ellipsis">${answer.description}</p>
				</div>
				<tags:lastTouchFor touchable="${answer}" />
			</div>
			
			<div
				class="${answer.flags.size() >= 5 ? 'heavy-flagged' : ''}"
				title="<fmt:message key='moderation.flagged.lots'/>">
			</div>
		</li>
	</c:forEach>
</ul>

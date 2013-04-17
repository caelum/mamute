<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>
<%@attribute name="commentVotes" type="br.com.caelum.brutal.model.CommentsAndVotes" required="true" %>
<c:set var="currentUserIsAuthor" value="${answer.author.id == currentUser.id}" />
<section class="post-area">
	<div class="post-meta">
		<tags:voteFor item="${answer}" type="resposta" vote="${vote}"/>
		<tags:solutionMarkFor answer="${answer}"/>
	</div>
	<div class="post-container">
		<div class="post-text" id="answer-${answer.id }">${answer.markedDescription}</div>
		<div class="post-interactions">
			<ul class="post-action-nav nav piped-nav">
				<li class="nav-item">
					<a class="post-action edit requires-login requires-karma"
							data-author="${currentUserIsAuthor}"
							data-karma="${EDIT_ANSWER}" 
							href="${linkTo[AnswerController].answerEditForm[answer.id]}"><fmt:message key="edit" /></a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser != null && !answer.alreadyFlaggedBy(currentUser)}">
						<a href="#" data-author="${answer.author.id == currentUser.id}"
							data-modal-id="answer-flag-modal${answer.id}" 
							data-karma="${CREATE_FLAG}" class="post-action author-cant requires-login flag-it requires-karma">
							<fmt:message key="flag" />
						</a>
					</c:if>
					<tags:flagItFor type="resposta" modalId="answer-flag-modal${answer.id}" flaggable="${answer}"/>
				</li>
			</ul>
			<tags:touchesFor touchable="${answer}" />
		</div>
		<tags:add-a-comment type="resposta" item="${answer}" votes="${commentVotes}"/>
		<c:if test="${currentUser.moderator && answer.hasPendingEdits()}">
			<a class="message alert" href="${linkTo[HistoryController].similarAnswers[answer.id]}"><fmt:message key="answer.warns.has_edits"/></a>
		</c:if>
	</div>
</section>
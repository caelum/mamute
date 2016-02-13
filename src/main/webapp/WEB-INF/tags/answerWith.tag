<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="answer" type="org.mamute.model.Answer" required="true" %>
<%@attribute name="vote" type="org.mamute.model.Vote" required="true" %>
<%@attribute name="commentVotes" type="org.mamute.model.CommentsAndVotes" required="true" %>
<section class="post-area ${answer.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<div class="post-meta">
		<tags:voteFor item="${answer}" type="${answer.typeName}" vote="${vote}"/>
		<tags:solutionMarkFor answer="${answer}"/>
	</div>
	<div class="post-container">
		<div class="post-text" id="answer-${answer.id }">${answer.markedDescription}</div>
		<div class="post-interactions">
			<ul class="post-action-nav nav piped-nav">
				<li class="nav-item">
					<a class="post-action edit requires-login requires-karma"
							data-author="${currentUser.current.isAuthorOf(answer)}"
							data-karma="${EDIT_ANSWER}" 
							href="${linkTo[AnswerController].answerEditForm(answer)}">${t['edit']}</a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser.loggedIn && !answer.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(answer)}"
							data-modal-id="answer-flag-modal${answer.id}" 
							data-karma="${CREATE_FLAG}" class="post-action author-cant requires-login flag-it requires-karma">
							${t['flag']}
						</a>
					</c:if>
					<tags:flagItFor type="${t['answer.type_name']}" modalId="answer-flag-modal${answer.id}" flaggable="${answer}"/>
				</li>
			</ul>
			<tags:touchesFor touchable="${answer}" />
		</div>
		<tags:add-a-comment type="${t['answer.type_name']}" item="${answer}" votes="${commentVotes}"/>
		<c:if test="${currentUser.moderator && answer.hasPendingEdits()}">
			<a class="message moderator-alert" href="${linkTo[HistoryController].similarAnswers(answer.id)}">${t['answer.warns.has_edits']}</a>
		</c:if>
		<c:if test="${env.supports('deletable.answers') and (currentUser.current.isAuthorOf(answer) or currentUser.moderator) and answer.deletable}">
			<a class="post-action delete-post" data-confirm-deletion="true" data-delete-form="delete-answer-form" href="#">Delete</a>
			<form class="hidden delete-answer-form" method="post" action="${linkTo[AnswerController].delete(answer)}">
				<input type="hidden" value="DELETE" name="_method">
			</form>
		</c:if>
	</div>
</section>
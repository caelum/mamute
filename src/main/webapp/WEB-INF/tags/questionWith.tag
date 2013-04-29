<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<%@attribute name="commentVotes" type="br.com.caelum.brutal.model.CommentsAndVotes" required="true" %>
<section itemscope itemtype="http://schema.org/Article" class="post-area question-area ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }" >
	<h1 itemprop="name" class="title subheader question-title"><c:out value="${question.title}" escapeXml="${true}"/></h1>
	<div class="post-meta">
		<tags:voteFor item="${question}" type="pergunta" vote="${currentVote}"/>
	</div>
	<div class="post-container">
		<div class="post-text question-description" id="question-description-${question.id }">${question.markedDescription}</div>
		<tags:tagsFor taggable="${question}"/>
		<div class="post-interactions">
			<ul class="post-action-nav piped-nav nav">
				<li class="nav-item">
					<a class="post-action show-popup" href="#">
						<fmt:message key="share"/>
					</a>
					<div class="popup share small">
						<form class="validated-form">
							<label for="share-url"><fmt:message key="share.text"/></label>
							<input type="text" class="text-input required" id="share-url" value="${currentUrl}"/>
						</form>
						<a target="_blank" class="share-button" data-shareurl="http://www.facebook.com/sharer/sharer.php?u=${currentUrl}"><i class="icon-facebook-sign icon-almost-3x"></i></a>
						<a target="_blank" class="share-button" data-shareurl="https://twitter.com/share?text=${question.title}&url=${currentUrl}"><i class="icon-twitter-sign icon-almost-3x"></i></a>
						<a class="close-popup"><fmt:message key="popup.close"/></a>
					</div>
				</li>
				<li class="nav-item">
					<a class="post-action edit-question 
					    requires-login requires-karma"
					    data-author="${currentUser.current.isAuthorOf(question)}"
					    data-karma="${EDIT_QUESTION}" 
					    href="<c:url value="/pergunta/editar/${question.id}" />">
						<fmt:message key="edit" />
					</a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser.loggedIn && !question.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(question)}" data-karma="${CREATE_FLAG}"
							data-modal-id="question-flag-modal${question.id}" 
							class="post-action author-cant requires-login flag-it requires-karma">
							<fmt:message key="flag" />
						</a>
					</c:if>
					<tags:flagItFor type="pergunta" modalId="question-flag-modal${question.id}" flaggable="${question}"/>
				</li>
			</ul>
			<tags:touchesFor touchable="${question}" />
		</div>
		<tags:add-a-comment type="pergunta" item="${question}" votes="${commentVotes}"/>
		<c:if test="${currentUser.moderator && question.hasPendingEdits()}">
			<a class="message moderator-alert" href="${linkTo[HistoryController].similarQuestions[question.id]}"><fmt:message key="question.warns.has_edits"/></a>
		</c:if>
	</div>
</section>
<ol id="intro">
	<tags:joyrideTip className="post-meta" options="tipLocation:right" key="intro.question.post_meta" />
	<tags:joyrideTip className="edit-question" options="tipLocation:bottom" key="intro.question.edit_question" />
	<tags:joyrideTip className="add-comment" options="tipLocation:bottom" key="intro.question.add_comment" />
	<tags:joyrideTip className="solution-tick" options="tipLocation:right" key="intro.question.solution_mark" />
</ol>

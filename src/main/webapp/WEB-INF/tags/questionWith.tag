<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="question" type="org.mamute.model.Question" required="true" %>
<%@attribute name="commentVotes" type="org.mamute.model.CommentsAndVotes" required="true" %>
<section itemscope itemtype="http://schema.org/Article" class="post-area question-area ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }" >
	<h1 itemprop="name" class="title subheader main-thread-title question-title"><c:out value="${question.title}" escapeXml="${true}"/></h1>
	
	<c:if test="${shouldShowAds && !markAsSolution && !showUpvoteBanner}">
		<tags:brutal-include value="questionTopAd" />
	</c:if>
	<div class="post-meta">
		<tags:voteFor item="${question}" type="${question.typeName}" vote="${currentVote}"/>
		<tags:watchFor watchable="${question}" type="${t['question.type_name']}"/>
	</div>
	<div class="post-container">
	<c:if test="${currentUser.moderator}">
		<a class="message moderator-link" href="${linkTo[QuestionController].showVoteInformation(question, question.sluggedTitle)}">${t['user.moderation.details']}</a><br/>
	</c:if>
		<div itemprop="articleBody" class="post-text question-description" id="question-description-${question.id }">
			${question.markedDescription}
		</div>
		<tags:tagsFor taggable="${question}"/>
		<div class="post-interactions">
			<ul class="post-action-nav piped-nav nav">
				<li class="nav-item">
					<a class="post-action show-popup" href="#">
						${t['share']}
					</a>
					<div class="popup share small">
						<form class="validated-form">
							<label for="share-url">${t['share.text']}</label>
							<input type="text" class="text-input required" id="share-url" value="${currentUrl}"/>
						</form>
						<a target="_blank" class="share-button" 
							data-shareurl="http://www.facebook.com/sharer/sharer.php?u=${currentUrl}">
							<i class="icon-facebook-squared icon-almost-3x"></i>
						</a>
						<a target="_blank" class="share-button" 
							data-shareurl="https://twitter.com/share?text=<c:out value="${question.title}" escapeXml="true" />&url=${currentUrl}">
							<i class="icon-twitter-squared icon-almost-3x"></i>
						</a>
						<a target="_blank" class="share-button" 
							data-shareurl="https://plus.google.com/share?&url=${currentUrl}">
							<i class="icon-gplus-squared icon-almost-3x"></i>
						</a>
						<a class="close-popup">${t['popup.close']}</a>
					</div>
				</li>
				<li class="nav-item">
					<a class="post-action edit-question 
					    requires-login requires-karma"
					    data-author="${currentUser.current.isAuthorOf(question)}"
					    data-karma="${EDIT_QUESTION}" 
					    href="${linkTo[QuestionController].questionEditForm(question)}">
						${t['edit']}
					</a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser.loggedIn && !question.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(question)}" data-karma="${CREATE_FLAG}"
							data-modal-id="question-flag-modal${question.id}" 
							class="post-action author-cant requires-login flag-it requires-karma">
							${t['flag']}
						</a>
					</c:if>
					<tags:flagItFor type="${t['question.type_name']}" modalId="question-flag-modal${question.id}" flaggable="${question}"/>
				</li>
				<c:if test="${env.supports('deletable.questions')}">
					<c:if test="${currentUser.current.isAuthorOf(question) and not currentUser.moderator and question.deletable}">
						<li class="nav-item">
							<a href="#" class="delete-post" data-confirm-deletion="true" data-delete-form="delete-question-form">${t['question.delete']}</a>
						</li>
						<form class="hidden delete-question-form" method="post" action="${linkTo[QuestionController].deleteQuestion(question)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
					<c:if test="${currentUser.moderator}">
						<li class="nav-item">
							<a href="#" class="delete-post" data-delete-form="delete-question-fully-form" data-confirm-deletion="true">
								${t['question.delete.fully']}
							</a>
						</li>
						<form class="hidden delete-question-fully-form" method="post" action="${linkTo[QuestionController].deleteQuestionFully(question)}">
							<input type="hidden" value="DELETE" name="_method">
						</form>
					</c:if>
				</c:if>
			</ul>
			<tags:touchesFor touchable="${question}" microdata="true"/>
		</div>
		<tags:add-a-comment type="${t['question.type_name']}" item="${question}" votes="${commentVotes}"/>
		<c:if test="${currentUser.moderator && question.hasPendingEdits()}">
			<a class="message moderator-alert" href="${linkTo[HistoryController].similarQuestions(question.id)}">${t['question.warns.has_edits']}</a>
		</c:if>
	</div>
</section>
<ol id="intro">
	<tags:joyrideTip className="post-meta" options="tipLocation:right" key="intro.question.post_meta" />
	<tags:joyrideTip className="edit-question" options="tipLocation:bottom" key="intro.question.edit_question" />
	<tags:joyrideTip className="add-comment" options="tipLocation:bottom" key="intro.question.add_comment" />
	<tags:joyrideTip className="solution-tick" options="tipLocation:right" key="intro.question.solution_mark" />
	<tags:joyrideTip className="about" options="tipLocation:bottom" key="intro.about" />
</ol>

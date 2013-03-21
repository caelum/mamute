<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>
<section class="post-area">
	<div class="post-meta">
		<tags:voteFor item="${answer}" type="resposta" vote="${vote}"/>
		<span class="icon-ok-sign icon-2x solution-mark container"></span>
		<c:if test="${answer.question.author.id == currentUser.id}">
			<a class="mark-as-solution requires-login"  href="${linkTo[AnswerController].markAsSolution}">
				<span class="icon-ok-sign icon-2x icon-muted container"></span>
			</a>
		</c:if>
	</div>
	<div class="post-container">
		<div class="post-text" id="answer-${answer.id }">${answer.markedDescription}</div>
		<div class="post-interactions">
			<ul class="post-action-nav nav piped-nav">
				<li class="nav-item">
					<a class="post-action edit requires-login requires-karma"
							data-author="${isAuthor}"
							data-karma="20" 
							href="${linkTo[AnswerController].answerEditForm[answer.id]}"><fmt:message key="edit" /></a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser != null && !answer.alreadyFlaggedBy(currentUser)}">
						<a href="#" data-author="${answer.author.id == currentUser.id}"
							data-modal-id="answer-flag-modal${answer.id}" 
							data-karma="10" class="post-action author-cant 
							requires-login flag-it requires-karma">
							<fmt:message key="flag" />
						</a>
					</c:if>
					<tags:flagItFor type="resposta" modalId="answer-flag-modal${answer.id}" flaggable="${answer}"/>
				</li>
			</ul>
			<tags:touchesFor touchable="${answer}" />
		</div>
		<tags:add-a-comment type="resposta" item="${answer}"/>
	</div>
</section>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="comment" type="br.com.caelum.brutal.model.Comment" required="true" %>
<%@attribute name="collapsed" type="java.lang.Boolean" required="true" %>
<%@attribute name="currentUserVote" type="br.com.caelum.brutal.model.Vote" required="false" %>

<c:set var="isCommentAuthor" value="${comment.author.id == currentUser.id}"/>
<li class="comment ${collapsed ? 'collapsed hidden' : ''} ${comment.isVisibleForModeratorAnd(currentUser) ? 'highlight-post' : '' }" id="comment-${comment.id}">
	<div class="post-meta comment-meta vote-container">
		<a title="<fmt:message key="comment.list.upvote"/>"  class="comment-meta-item container comment-option author-cant requires-login vote-option icon-chevron-up 
			${(not empty currentUserVote) ? 'voted' : '' }" 
			data-value="positivo" data-author="${isCommentAuthor}" 
			data-type="comentario" data-id="${comment.id}">
		</a>
		<span class="comment-meta-item vote-count comment-vote-count">${comment.voteCount}</span>
		<c:if test="${currentUser != null && !comment.alreadyFlaggedBy(currentUser) && !isCommentAuthor}">
			<a title="<fmt:message key="flag"/>" href="#" data-author="${isCommentAuthor}"
			data-modal-id="comment-flag-modal${comment.id}"
			class="comment-meta-item container author-cant requires-login comment-option flag-it icon-flag"></a>
		</c:if>
	</div>
	<div class="post-container comment-container">
		<span>
			${comment.htmlComment}
		</span> &#8212;
		
		<tags:userProfileLink user="${comment.author}" htmlClass="${comment.author.id eq item.author.id ? 'same-author' : ''}"/> 
		&nbsp;<tags:prettyTime time="${comment.lastUpdatedAt}"/>
	
		<fmt:message  key="edit_form.submit" var="submit"/>
		<c:if test="${comment.author.id == currentUser.id}">
			<tags:simpleAjaxFormWith action="${linkTo[CommentController].edit[comment.id]}" 
				field="comment" onCallback="replace" callbackTarget="comment-${comment.id}" 
				submit="${submit}" value="${comment.comment}">
				<a class="requires-login requires-karma" data-author="${item.author.id eq currentUser.id }" href="#">
					<fmt:message key="edit.link" />
				</a>
			</tags:simpleAjaxFormWith>
		</c:if>

<%-- 		<c:if test="${comment.author.id == currentUser.id}"> --%>
<%-- 			<tags:editFor onCallback="replace" item="${comment}" field="comment" value="${comment.comment}" ajaxResult="comment-${comment.id}" /> --%>
<%-- 		</c:if> --%>
	</div>	
</li>
<tags:flagItFor type="comentario" modalId="comment-flag-modal${comment.id}" flaggable="${comment}"/>














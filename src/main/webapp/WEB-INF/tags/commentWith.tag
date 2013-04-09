<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="comment" type="br.com.caelum.brutal.model.Comment" required="true" %>
<%@attribute name="collapsed" type="java.lang.Boolean" required="true" %>

<c:set var="isCommentAuthor" value="${comment.author.id == currentUser.id}"/>
<li class="comment ${collapsed ? 'collapsed hidden' : ''}">
	<div class="post-meta comment-meta vote-container">
		<a class="comment-meta-item container comment-option author-cant requires-login vote-option icon-chevron-up 
			${(comment.voteCount > 0) ? 'voted' : '' }" 
			data-value="positivo" data-author="${isCommentAuthor}" 
			data-type="comentario" data-id="${comment.id}">
		</a>
		<span class="comment-meta-item vote-count comment-vote-count">${comment.voteCount}</span>
		<c:if test="${currentUser != null && !comment.alreadyFlaggedBy(currentUser) && !isCommentAuthor}">
			<a href="#" data-author="${isCommentAuthor}"
			data-modal-id="comment-flag-modal${comment.id}"
			class="comment-meta-item container author-cant requires-login comment-option flag-it icon-flag"></a>
		</c:if>
	</div>
	<div class="post-container comment-container">
		<span id="comment-${comment.id}">
			${comment.htmlComment}
		</span> &#8212;
		
		<tags:userProfileLink user="${comment.author}" htmlClass="${comment.author.id eq item.author.id ? 'same-author' : ''}"/> 
		&nbsp;<tags:prettyTime time="${comment.lastUpdatedAt}"/>

		<c:if test="${comment.author.id == currentUser.id}">
			<tags:editFor item="${comment}" field="comment" value="${comment.comment}" ajaxResult="comment-${comment.id}" />
		</c:if>
	</div>	
</li>
<tags:flagItFor type="comentario" modalId="comment-flag-modal${comment.id}" flaggable="${comment}"/>
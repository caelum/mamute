<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Commentable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>

<c:set var="ajaxResultName" value="new-comment-for-${type}-new-comment-${item.id}"/>

<ul class="comment-list ${empty item.comments ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.comments }" varStatus="status">
		<c:set var="isCommentAuthor" value="${comment.author.id == currentUser.id}"/>
		<c:choose>
			<c:when test="${status.count > 5}"><li class="comment collapsed hidden"></c:when>
			<c:otherwise><li class="comment"></c:otherwise>
		</c:choose>
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
	</c:forEach>
</ul>

<c:set var="commentsSize" value="${fn:length(item.comments)}"/>
<c:if test="${commentsSize > 5}">
	<span class="more-comments" size="${commentsSize}">Mostrar todos os <strong>${commentsSize}</strong> comentários</span>
</c:if>

<div class="edit-via-ajax">
	<a href="#" class="requires-login post-action add-comment requires-karma" data-karma="${CREATE_COMMENT}">
		<fmt:message key="comment.submit" />
	</a>
	<span>
		<form action="${linkTo[CommentController].comment[item.id][type]}" 
			class="validated-form ajax hinted-form" data-ajax-result="${ajaxResultName}" 
			data-ajax-on-callback="append">
			<label for="comment"><fmt:message key="comment.label" /></label>
			<textarea id="comment" class="text-input required to-focus hintable" minlength="15" name="message" data-hint-id="${ajaxResultName}-hint"></textarea>
			<input type="submit" class="post-submit comment-submit" value="<fmt:message key="comment.submit"/>" />
		</form>
		<div class="form-hints">
			<span class="hint" id="${ajaxResultName}-hint"><fmt:message key="comment.hint"/></span>
		</div>
	</span>
</div>

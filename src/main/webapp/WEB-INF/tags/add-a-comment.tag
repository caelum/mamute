<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Commentable" required="true" %>
<c:set var="ajaxResultName" value="new-comment-for-${item.typeName}-new-comment-${item.id}"/>
<ul class="comment-container ${empty item.comments ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.comments }">
		<li class="comment">
			<span id="comment-${comment.id}">${comment.htmlComment}</span> &#8212;
			<tags:userProfileLink user="${comment.author}" htmlClass="${comment.author.id eq item.author.id ? 'same-author' : ''}"/> 
			&nbsp;<tags:prettyTime time="${comment.lastUpdatedAt}"/>
			<c:if test="${comment.author.id == currentUser.id }">
				<tags:editFor item="${comment}" field="comment" value="${comment.comment}" ajaxResult="comment-${comment.id}" />
			</c:if>
		</li>
	</c:forEach>
</ul>

<div class="edit-via-ajax">
	<a href="#" class="requires-login post-action"><fmt:message key="comment.add_comment" /></a>
	<span>
		<form action="<c:url value="/${item.typeName}/${item.id}/comment"/>" class="validated-form ajax hinted-form" data-ajax-result="${ajaxResultName}" data-ajax-on-callback="append">
			<label for="comment"><fmt:message key="comment.add_comment" /></label>
			<textarea id="comment" class="required to-focus hintable" minlength="15" name="message" data-hint-id="${ajaxResultName}-hint"></textarea>
			<input type="submit" class="post-submit comment-submit" value="<fmt:message key="comment.add_comment"/>" />
		</form>
		<div class="form-hints">
			<span class="hint" id="${ajaxResultName}-hint"><fmt:message key="comment.text.hint"/></span>
		</div>
	</span>
</div>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Commentable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="ajaxResultName" value="new-comment-for-${type}-new-comment-${item.id}"/>
<ul class="comment-list ${empty item.comments ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.comments }" varStatus="status">
		<tags:commentWith comment="${comment}" collapsed="${status.count > 5}"/>
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
	<span class="post-container comment-container">
		<form action="${linkTo[CommentController].comment[item.id][type]}" 
			class="validated-form ajax" data-ajax-result="${ajaxResultName}" 
			data-ajax-on-callback="append">
			<label for="comment"><fmt:message key="comment.label" /></label>
			<textarea id="comment" class="text-input required to-focus hintable comment-textarea" minlength="15" maxlength="600" name="message" data-hint-id="${ajaxResultName}-hint"></textarea>
			<input type="submit" class="post-submit comment-submit" value="<fmt:message key="comment.submit"/>" />
		</form>
	</span>
	<span class="hint" id="${ajaxResultName}-hint"><fmt:message key="comment.hint"/></span>
</div>

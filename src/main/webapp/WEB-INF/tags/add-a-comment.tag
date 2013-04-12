<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Commentable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="votes" type="br.com.caelum.brutal.model.CommentsAndVotes" required="true" %>

<c:set var="ajaxResultName" value="new-comment-for-${type}-new-comment-${item.id}"/>
<ul class="comment-list ${empty item.comments ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.comments}" varStatus="status">
		<c:if test="${not comment.invisible || currentUser.moderator}">
			<tags:commentWith comment="${comment}" collapsed="${status.count > 5}" currentUserVote="${votes.getVotes(comment)}"/>
		</c:if>
	</c:forEach>
</ul>

<c:set var="commentsSize" value="${fn:length(item.comments)}"/>
<c:if test="${commentsSize > 5}">
	<span class="more-comments" size="${commentsSize}">Mostrar todos os <strong>${commentsSize}</strong> comentários</span>
</c:if>


<fmt:message  key="comment.submit" var="submit"/>
<tags:simpleAjaxFormWith action="${linkTo[CommentController].comment[item.id][type]}" 
field="comment" onCallback="append" callbackTarget="${ajaxResultName}" submit="${submit}">
	<a href="#" class="requires-login post-action add-comment requires-karma" data-karma="${CREATE_COMMENT}">
		${submit}
	</a>
</tags:simpleAjaxFormWith>

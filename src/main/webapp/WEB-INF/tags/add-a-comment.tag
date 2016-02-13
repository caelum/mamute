<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="item" type="org.mamute.model.interfaces.Commentable" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="votes" type="org.mamute.model.CommentsAndVotes" required="true" %>
<%@attribute name="groupComments" type="java.lang.Boolean" required="false" %>
<%@attribute name="startFormHidden" type="java.lang.Boolean" required="false" %>

<c:if test="${empty groupComments}">
	<c:set var="groupComments" value="true" />
</c:if>
<c:if test="${empty startFormHidden}">
	<c:set var="startFormHidden" value="true" />
</c:if>

<c:set var="ajaxResultName" value="new-comment-for-${type}-new-comment-${item.id}"/>
<ul class="comment-list ${empty item.getVisibleCommentsFor(currentUser.current) ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.getVisibleCommentsFor(currentUser.current)}" varStatus="status">
		<tags:commentWith type="${type}" item="${item}" comment="${comment}" collapsed="${status.count > 5 && groupComments}" currentUserVote="${votes.getVotes(comment)}"/>
	</c:forEach>
</ul>

<c:set var="commentsSize" value="${fn:length(item.getVisibleCommentsFor(currentUser.current))}"/>
<c:if test="${commentsSize > 5  && groupComments}">
	<span class="more-comments" size="${commentsSize}">
		<c:set var="commentSizeBold" value="<strong>${commentsSize}</strong>"/>
		${t["comment.show_all"].args(commentSizeBold)}>
	</span>
</c:if>


<c:set value="${t['comment.submit']}" var="submit"/>
<tags:simpleAjaxFormWith startHidden="${startFormHidden}" action="${linkTo[CommentController].comment(item.id, type, '', false)}" field="comment" 
onCallback="append" callbackTarget="${ajaxResultName}" submit="${submit}">
	<a href="#" class="requires-login post-action add-comment requires-karma" data-karma="${CREATE_COMMENT}">
		${submit}
	</a>
</tags:simpleAjaxFormWith>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="list" type="java.util.List" required="true" %>

<ul>
	<h1 class="flagged-item-title-moderator">Comentarios</h1>
	<c:forEach var="flaggableComment" items="${list}">
	<c:set var="comment" value="${flaggableComment.flaggable}"/>
		<li class="post-item question-item ${comment.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
			
			<div class="post-information question-information">
				<tags:postItemInformation key="post.list.vote"
					count="${comment.voteCount}" information="votes"
					htmlClass="question-info" />
			</div>
			
			<div class="summary question-summary">
				<div class="item-title-wrapper">
					<h5 class="">Flags: ${comment.flags.size()}</h5>
					<h3 class="title item-title main-thread-title question-title">
						${comment.comment}
						<!-- Usar essa linha para fazer o link depois, teriam q ser feitas queries -->
						<%-- <tags:questionLinkFor question="${ type.equals('questionType') ? comment : comment.question }" /> --%>
					</h3>
				</div>
			</div>
			
			<div
				class="${comment.flags.size() >= 5 ? 'heavy-flagged' : ''}"
				title="<fmt:message key='moderation.flagged.lots'/>">
			</div>
		</li>
	</c:forEach>
</ul>
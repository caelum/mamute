<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.interfaces.Commentable" required="true" %>

<c:set var="ajaxResultName" value="new-comment-for-${item.typeName}-new-comment-${item.id}"/>

<ul class="comment-container ${empty item.comments ? 'hidden' : ''}" id="${ajaxResultName }">
	<c:forEach var="comment" items="${item.comments }">
		<li class="comment">
			<c:if test="${currentUser != null && !comment.alreadyFlaggedBy(currentUser)}">
				<div class="comment-options hidden">
					<a href="#" class="flag-it icon-flag"></a>
				</div>
			</c:if>
			<div class="hidden modal modal-flag">
				<form class="validated-form" action="${linkTo[FlagController].addFlag[comment.id]}">
					<input type="radio" value="RUDE" name="flagType" id="flag-type-rude" />
					<label for="flag-type-rude"><fmt:message key="comment.flag.rude" /></label>
					
					<input type="radio" value="NOT_CONSTRUCTIVE" name="flagType" id="flag-type-notconstructive" />
					<label for="flag-type-notconstructive"><fmt:message key="comment.flag.not_constructive" /></label>
						
					<input type="radio" value="OBSOLETE" name="flagType" id="flag-type-obsolete" />
					<label for="flag-type-obsolete"><fmt:message key="comment.flag.obsolete" /></label>
					
					<input class="other-option" type="radio" value="OTHER" name="flagType" id="flag-type-other" data-reason-id="other-reason" />
					<label for="flag-type-other"><fmt:message key="comment.flag.other" /></label>
	
					<div id="other-reason" class="hidden">
						<label for="reason-text"><fmt:message key="comment.flag.other.reason" /></label>
						<textarea id="reason-text" minlength="6" name="reason"></textarea>
					</div>
				
					<input type="submit" value="<fmt:message key="flag.submit" />"/>
				</form>
			</div>
			<span id="comment-${comment.id}">
				${comment.htmlComment}
			</span> &#8212;
			
			<tags:userProfileLink user="${comment.author}" htmlClass="${comment.author.id eq item.author.id ? 'same-author' : ''}"/> 
			&nbsp;<tags:prettyTime time="${comment.lastUpdatedAt}"/>
			<c:if test="${comment.author.id == currentUser.id }">
				<tags:editFor item="${comment}" field="comment" value="${comment.comment}" ajaxResult="comment-${comment.id}" />
			</c:if>
		</li>
	</c:forEach>
</ul>

<div class="edit-via-ajax">
	<a href="#" class="requires-login post-action"><fmt:message key="comment.submit" /></a>
	<span>
		<form action="<c:url value="/${item.typeName}/${item.id}/comment"/>" class="validated-form ajax hinted-form" data-ajax-result="${ajaxResultName}" data-ajax-on-callback="append">
			<label for="comment"><fmt:message key="comment.label" /></label>
			<textarea id="comment" class="text-input required to-focus hintable" minlength="15" name="message" data-hint-id="${ajaxResultName}-hint"></textarea>
			<input type="submit" class="post-submit comment-submit" value="<fmt:message key="comment.submit"/>" />
		</form>
		<div class="form-hints">
			<span class="hint" id="${ajaxResultName}-hint"><fmt:message key="comment.hint"/></span>
		</div>
	</span>
</div>

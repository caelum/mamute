<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="flaggable" type="br.com.caelum.brutal.model.interfaces.Flaggable" required="true" %>

<div class="comment-options vote-container comment-vote">
	<a class="requires-author-cant vote-option icon-chevron-up important-hidden
		${(flaggable.voteCount > 0) ? 'voted' : '' }" 
		data-value="up" data-author="${flaggable.author.id == currentUser.id}" 
		data-type="comment" data-id="${flaggable.id}">
	</a>
	<span class="vote-count comment-vote-count">${flaggable.voteCount}</span>
	<c:if test="${currentUser != null && !flaggable.alreadyFlaggedBy(currentUser)}">
		<a href="#" class="flag-it icon-flag important-hidden"></a>
	</c:if>
</div>
<div class="hidden modal modal-flag">
	<form class="validated-form" action="${linkTo[FlagController].addFlag[flaggable.class.simpleName][flaggable.id]}">
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
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="flaggable" type="br.com.caelum.brutal.model.interfaces.Flaggable" required="true" %>

<c:if test="${currentUser != null && !flaggable.alreadyFlaggedBy(currentUser)}">
	<div class="comment-options hidden">
		<a href="#" class="flag-it icon-flag"></a>
	</div>
</c:if>
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
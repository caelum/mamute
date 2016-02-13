<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="flaggable" type="org.mamute.model.interfaces.Flaggable" required="true" %>
<%@attribute name="modalId" type="java.lang.String" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>

<div class="hidden modal modal-flag" id="${modalId}">
	<ul class="nav">
		<li class="modal-close-button">x</li>
	</ul>
	<form class="validated-form" action="${linkTo[FlagController].addFlag(type, flaggable.id, null, '')}">
		<input type="radio" value="RUDE" name="flagType" id="flag-type-rude" />
		<label for="flag-type-rude">${t['comment.flag.rude']}</label>
		
		<input type="radio" value="NOT_CONSTRUCTIVE" name="flagType" id="flag-type-notconstructive" />
		<label for="flag-type-notconstructive">${t['comment.flag.not_constructive']}</label>
			
		<input type="radio" value="OBSOLETE" name="flagType" id="flag-type-obsolete" />
		<label for="flag-type-obsolete">${t['comment.flag.obsolete']}</label>
		
		<input class="other-option" type="radio" value="OTHER" name="flagType" id="flag-type-other" data-reason-id="other-reason" />
		<label for="flag-type-other">${t['comment.flag.other']}</label>

		<div id="other-reason" class="hidden">
			<label for="reason-text">${t['comment.flag.other.reason']}</label>
			<textarea id="reason-text" name="reason"></textarea>
		</div>
		<label class="error"></label>
	
		<input type="submit" value="${t['flag.submit']}"/>
	</form>
</div>

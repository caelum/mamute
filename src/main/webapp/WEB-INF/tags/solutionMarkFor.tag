<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<div class="${answer.solution ? 'solution-container' : 'not-solution-container'}">
	<c:if test="${answer.question.author.id == currentUser.id}">
		<a class="mark-as-solution requires-login
		icon-ok-sign icon-2x icon-muted container solution-tick" 
		href="${linkTo[AnswerController].markAsSolution[answer.id]}">
		</a>
		<span class="mark-as-solution-subtitle">Marcar como certa</span>
	</c:if>
	<c:if test="${answer.solution && answer.question.author.id != currentUser.id}">
		<span class="icon-ok-sign icon-2x icon-muted container solution-tick"></span>
	</c:if>
</div>
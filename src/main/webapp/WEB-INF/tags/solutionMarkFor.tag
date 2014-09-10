<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="answer" type="org.mamute.model.Answer" required="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="${answer.solution ? 'solution-container' : 'not-solution-container'}">
	<c:if test="${currentUser.current.isAuthorOf(answer.question)}">
		<a class="mark-as-solution requires-login" href="${linkTo[AnswerController].markAsSolution(answer.id)}">
			<span class="icon-ok-circled icon-2x icon-muted container solution-tick"></span>
			<span class="mark-as-solution-subtitle">${t['answer.mark_as_solution']}</span>
		</a>
	</c:if>
	<c:if test="${answer.solution && !currentUser.current.isAuthorOf(answer.question)}">
		<span class="icon-ok-circled icon-2x icon-muted container solution-tick"></span>
	</c:if>
</div>
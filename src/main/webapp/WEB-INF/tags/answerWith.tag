<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>
<li class="answer ${answer.solution? 'solution' : '' }" data-id="${answer.id}">
	<p id="answer-${answer.id }">${answer.markedDescription}</p>
	<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
		<fmt:message key="answer.mark_as_solution" />
	</a>
	(<a href="<c:url value="/answer/edit/${answer.id }"/>"><fmt:message key="edit" /></a>)
	<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
	<tags:add-a-comment item="${answer}"/>
</li>

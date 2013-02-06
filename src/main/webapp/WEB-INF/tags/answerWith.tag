<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>
<li class="post-area answer ${answer.solution? 'solution' : '' }" data-id="${answer.id}">
	<p class="post-text" id="answer-${answer.id }">${answer.markedDescription}</p>
	<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
	(<a href="<c:url value="/answer/edit/${answer.id }"/>"><fmt:message key="edit" /></a>)
	<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
		<fmt:message key="answer.mark_as_solution" />
	</a>
	<tags:add-a-comment item="${answer}"/>
</li>

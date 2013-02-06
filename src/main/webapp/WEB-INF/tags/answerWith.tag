<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="answer" type="br.com.caelum.brutal.model.Answer" required="true" %>
<%@attribute name="vote" type="br.com.caelum.brutal.model.Vote" required="true" %>
<li class="post-area answer ${answer.solution? 'solution' : ''}" data-id="${answer.id}">
	<div class="post-meta">
		<tags:voteFor item="${answer}" type="answer" vote="${vote}"/>
		<c:if test="${answer.solution}">
			<div>CERTO</div>
		</c:if>
	</div>
	<div class="post-container">
		<p class="post-text" id="answer-${answer.id }">${answer.markedDescription}</p>
		<ul class="post-action-nav nav">
			<li class="nav-item">
				<a class="post-action small" href="<c:url value="/question/edit/${question.id}"/>"><fmt:message key="edit" /></a>
			</li>
		</ul>
		<a class="mark-as-solution" href="${linkTo[AnswerController].markAsSolution}">
			<fmt:message key="answer.mark_as_solution" />
		</a>
		<tags:add-a-comment item="${answer}"/>
	</div>
</li>

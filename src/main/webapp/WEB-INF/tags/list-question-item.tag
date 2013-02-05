<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<div class="question">
	<div class="votes">${question.voteCount}</div>
	<div class="answers">${question.answersCount}</div>
	<div class="views">${question.views}</div>
	<div>
		<a href="<c:url value="/questions/${question.id }/${question.sluggedTitle }"/>">
				${question.title}
		</a>
	</div>
	<div>tags</div>
	<div><tags:prettyTime time="${question.lastUpdatedAt }" /></div>
	<div>${question.lastTouchedBy.name } ${question.lastTouchedBy.karma }</div>
	<div class="author">${question.author.name } ${question.author.karma }</div>
</div>

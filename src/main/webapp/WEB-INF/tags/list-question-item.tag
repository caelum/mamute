<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<li class="question">
	<div class="votes info">${question.voteCount}
		<tags:subtitleFor key="question.list.vote" count="${question.voteCount}"/>
	</div>
	<div class="answers info">${question.answersCount}
		<tags:subtitleFor key="question.list.answer" count="${question.answersCount}"/>
	</div>
	<div class="views info">${question.views}
		<tags:subtitleFor key="question.list.view" count="${question.views}"/>
	</div>
	<div class="summary">
		<h3 class="question-title">
			<a href="<c:url value="/questions/${question.id }/${question.sluggedTitle }"/>">
					${question.title}
			</a>
		</h3>
		<div class="tags">
			<a href="<c:url value="/list/withTag/1/java"/>" class="tag">java</a>
		</div>
		
		<div class="stats">
			<div><tags:prettyTime time="${question.lastUpdatedAt }" /></div>
			<div>${question.lastTouchedBy.name } ${question.lastTouchedBy.karma }</div>
			<div class="author"><a href="#">${question.author.name }</a> ${question.author.karma }</div>
		</div>
	</div>		
</li>

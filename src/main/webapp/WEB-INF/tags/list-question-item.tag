<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<li class="question">
	<div class="question-information">
		<tags:questionInformation key="question.list.vote" count="${question.voteCount}" information="votes"/>
		<tags:questionInformation key="question.list.answer" count="${question.answersCount}" information="answers ${question.solved ? 'solved' : ''} ${question.answersCount >= 1 ? 'answered' : ''}"/>
		<tags:questionInformation key="question.list.view" count="${question.views}" information="views"/>
	</div>
	<div class="summary">
		<h3 class="title item-title">
			<a href="<c:url value="/questions/${question.id }/${question.sluggedTitle}"/>">
					${question.title}
			</a>
		</h3>
		<tags:tagsFor question="${question}"/>
		<div class="stats">
			<span class="last-updated-at"><tags:prettyTime time="${question.lastUpdatedAt }" /></span>
			<a href="#">${question.lastTouchedBy.name }</a> 
			<span class="reputation">${question.lastTouchedBy.karma }</span>
		</div>
	</div>		
</li>

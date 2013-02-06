<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<li class="question">
	<tags:questionInformation key="question.list.vote" count="${question.voteCount}" information="votes"/>
	<tags:questionInformation key="question.list.answer" count="${question.answersCount}" information="answers"/>
	<tags:questionInformation key="question.list.view" count="${question.views}" information="views"/>
	<div class="summary">
		<h3 class="title item-title">
			<a href="<c:url value="/questions/${question.id }/${question.sluggedTitle}"/>">
					${question.title}
			</a>
		</h3>
		<tags:tagsFor question="${question}"/>
		<div class="stats">
			<span class="small last-updated-at"><tags:prettyTime time="${question.lastUpdatedAt }" /></span>
			<a class="small" href="#">${question.lastTouchedBy.name }</a> 
			<span class="reputation small">${question.lastTouchedBy.karma }</span>
		</div>
	</div>		
</li>

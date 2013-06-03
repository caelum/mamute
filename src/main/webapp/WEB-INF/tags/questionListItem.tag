<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<li class="question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<div class="question-information">
		<tags:questionInformation key="question.list.vote" count="${question.voteCount}" information="votes"/>
		<tags:questionInformation key="question.list.answer" count="${question.answersCount}" information="answers ${question.solved ? 'solved' : ''} ${question.answersCount >= 1 ? 'answered' : ''}"/>
		<tags:questionInformation key="question.list.view" count="${question.views}" information="views"/>
	</div>
	<div class="summary">
		<div class="item-title-wrapper">
			<h3 class="title item-title">
				<tags:questionLinkFor question="${question}"/>
			</h3>
			<tags:tagsFor taggable="${question}"/>
		</div>
		<tags:lastTouchFor touchable="${question}"/>
	</div>		
</li>

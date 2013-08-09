<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>

<li class="post-item question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<div class="post-information question-information">
		<tags:postItemInformation key="post.list.vote" count="${question.voteCount}" information="votes" htmlClass="question-info"/>
		<tags:postItemInformation key="question.list.answer" count="${question.answersCount}" information="answers ${question.solved ? 'solved' : ''} ${question.answersCount >= 1 ? 'answered' : ''}"  htmlClass="question-info"/>
	</div>
	<div class="summary question-summary">
		<div class="item-title-wrapper">
			<h3 class="title item-title main-thread-title question-title">
				<tags:questionLinkFor question="${question}"/>
			</h3>
			<tags:tagsFor taggable="${question}"/>
			<div class="post-simple-information">
				${question.views} <tags:pluralize key="post.list.view" count="${question.views}"/>
			</div>
		</div>
		<tags:lastTouchFor touchable="${question}"/>
	</div>		
</li>

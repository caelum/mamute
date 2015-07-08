<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="question" type="org.mamute.model.Question" required="true" %>
<%@attribute name="simple" type="java.lang.Boolean" required="false" %>

<c:if test="${simple == null}">
	<c:set var="simple" value="false"/>
</c:if>

<li class="post-item question-item ${question.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
	<div class="post-information question-information">
		<c:set scope="request" value="${question}" var="currentQuestion"/>
		<tags:brutal-include value="questionStats" />
	</div>
	<div class="summary question-summary">
		<div class="item-title-wrapper">
			<h3 class="title item-title main-thread-title question-title">
				<tags:questionLinkFor question="${question}"/>
			</h3>
			<c:if test="${!simple}">
				<tags:tagsFor taggable="${question}"/>
			</c:if>
			<div class="post-simple-information">
				${question.views} <tags:pluralize key="post.list.view" count="${question.views}"/>
			</div>
		</div>
		<c:if test="${!simple}">
			<tags:lastTouchFor touchable="${question}"/>
		</c:if>
	</div>
	<c:if test="${!simple}">
		<div class="${question.hasInteraction(currentUser.current) ? 'interaction' : ''}" title="${t['user.interactions']}"> </div>
	</c:if>
</li>

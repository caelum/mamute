<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="tagList" fragment="true" required="false"%>
<%@attribute name="information" type="org.mamute.model.Information" required="true" %>
<%@attribute name="index" type="java.lang.Integer" required="true" %>
<%@attribute name="type" type="java.lang.String" required="true" %>
<%@attribute name="isHistoryQuestion" type="java.lang.Boolean" required="false" %>

<div class="history-forms-area ${index != 0 ? 'hidden' : ''}">
	<form method="post" class="history-form moderate-form" action="${linkTo[HistoryController].publish(0, type, 0, '')}">
		<c:if test="${information.beforeCurrent && !isHistoryQuestion}">
			<p class="alert">${t['moderation.version_before_current']}</p>
		</c:if>
	
		<a href="#" class="toggle-version">${t['moderation.formatted']}</a>
		<a href="#" class="toggle-version hidden">${t['moderation.diff']}</a>
		<jsp:doBody/>
		<div class="history-diff post-text"></div>
		<div class="history-version"><jsp:invoke fragment="tagList"/></div>
	
		<ul class="post-touchs clear">
			<li class="touch author-touch">
				<tags:completeUser user="${information.author}">
					<div class="when" itemprop="dateCreated">
						${t['touch.created']}
						<tags:prettyTime time="${information.createdAt}"/>
					</div>
				</tags:completeUser>
			</li>
		</ul>
	
		<h2 class="history-title page-title">${t['moderation.comment']}</h2>
		<p class="post-text">
			${information.comment}
		</p>
		
		<c:if test="${!isHistoryQuestion}">
				<c:if test="${information.beforeCurrent}">
					<p class="alert">${t['moderation.version_before_current']}</p>
				</c:if>
		
				<input type="hidden" name="aprovedInformationType" value="${information.typeName}"/>
				<input type="hidden" name="moderatableId" value="${information.moderatable.id}"/>
				<input type="hidden" name="aprovedInformationId" value="${information.id}"/>
				<input type="submit" class="post-submit big-submit submit" value='${t['moderation.accept']}' />
			</form>
			<form method="post" action="${linkTo[HistoryController].reject(information.id,information.typeName)}">
				<input type="submit" class="post-submit big-submit submit" value='${t['moderation.reject']}' />
		</c:if>
	</form>
</div>
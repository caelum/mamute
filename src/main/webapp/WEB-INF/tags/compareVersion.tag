<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="isHistoryQuestion" type="java.lang.Boolean" required="false" %>
<c:set var="title" value="${t['metas.moderate_question.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="history-comparison">
	<div class="history-current">
		<h2 class="history-title page-title title">${t['moderation.current_version']}:</h2>
		<h2 class="title main-thread-title"><tags:questionLinkFor question="${post.information.question}"/></h2>
		<div class="post-text">
			${post.information.markedDescription}
		</div>
		<tags:tagsFor taggable="${post.information}"/>
	</div>
	<div class="history-edited">
		<c:if test="${empty histories}">
			<h2 class="alert">${t['moderation.no_versions']}</h2>
		</c:if>
		<c:if test="${!empty histories}">
			<tags:historiesSelect histories="${histories}" />
			<c:forEach items="${histories}" var="information" varStatus="status">
				
				<tags:historyForm index="${status.index}" information="${information}" type="${type}" isHistoryQuestion="${isHistoryQuestion}">
					<jsp:attribute name="tagList">
						<tags:tagsFor taggable="${information}"/>
					</jsp:attribute>
					<jsp:body>
						<h2 class="title main-thread-title">${information.title}</h2>
						<div class="history-version hidden">
							<div class="post-text">
								${information.markedDescription}
							</div>
							<tags:tagsFor taggable="${information}"/>
						</div>
					</jsp:body>
				</tags:historyForm>
			</c:forEach>
		</c:if>
	</div>
</div>

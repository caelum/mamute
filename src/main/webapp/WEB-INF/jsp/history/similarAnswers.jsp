<fmt:message key="metas.moderate_answer.title" var="title"/>
<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<div class="history-original">
	<a href="#" class="dropdown-trigger" data-target-id="question-original"><fmt:message key="moderation.show_question" /></a>
	<div id="question-original" class="dropdown-target">
		<h2 class="title main-thread-title"><tags:questionLinkFor answer="${post.information.answer}"/></h2>
		<div class="post-text">
			${post.information.markedDescription}
		</div>
	</div>
</div>
<div class="history-comparison">
	<div class="history-current">
		<h2 class="title main-thread-title"><tags:questionLinkFor answer="${post.information.answer}"/></h2>
		<div class="post-text">
			${post.information.markedDescription}
		</div>
		
	</div>
	
	<div class="history-edited">
		<c:if test="${empty histories}">
			<h2 class="alert"><fmt:message key="moderation.no_versions" /></h2>
		</c:if>
		<c:if test="${!empty histories}">
			<tags:historiesSelect histories="${histories}" />
			<c:forEach items="${histories}" var="information" varStatus="status">
				<tags:historyForm index="${status.index}" information="${information}" type="${type}">		
					<div class="history-version hidden">
						<h2 class="title main-thread-title"><tags:questionLinkFor answer="${information.answer}"/></h2>
						<div class="post-text">
							${information.markedDescription}
						</div>
					</div>
				</tags:historyForm>
			</c:forEach>
		</c:if>
	</div>
</div>
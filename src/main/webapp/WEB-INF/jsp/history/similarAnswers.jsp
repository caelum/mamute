<c:set var="title" value="${t['metas.moderate_answer.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div class="history-original">
	<a href="#" class="dropdown-trigger" data-target-id="question-original">${t['moderation.show_question']}</a>
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
			<h2 class="alert">${t['moderation.no_versions']}</h2>
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
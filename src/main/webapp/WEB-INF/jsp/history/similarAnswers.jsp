<fmt:message key="metas.moderate_answer.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<div class="history-original">
	<a href="#" class="dropdown-trigger" data-target-id="question-original"><fmt:message key="moderation.show_question" /></a>
	<div id="question-original" class="dropdown-target">
		<tags:simpleVersionFor questionInformation="${post.question.information}"/>
	</div>
</div>
<div class="history-comparison">
	<div class="history-current">
		<tags:simpleVersionFor answerInformation="${post.information}"/>
	</div>
	
	<div class="history-edited">
		<tags:historiesSelect histories="${histories}" />
		
		<c:forEach items="${histories}" var="information" varStatus="status">
			<tags:historyForm index="${status.index}" information="${information}" type="${type}">		
				<tags:simpleVersionFor answerInformation="${information}"/>
			</tags:historyForm>
		</c:forEach>
	</div>
</div>
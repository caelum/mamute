<select class="question-history-select">
	<option>selecione a versão</option>
	<c:forEach items="${histories}" var="information" varStatus="status">
		<option value="${status.index}">
			${information.author.name} às
			<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
				time="${information.createdAt}"></tags:jodaTime>
		</option>
	</c:forEach>
</select>

<c:forEach items="${histories}" var="information">

	<div class="forms hidden">
		<form method="post" class="moderate-form" action="${linkTo[HistoryController].publish[information.question.id][information.id]}">
			<h2>
				${information.author.name} às
				<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
					time="${information.createdAt}"></tags:jodaTime>
			</h2>
			
			<p>
				<fmt:message key="newquestion.title"/>:
				${information.title}
			</p>
			<p>
				${information.markedDescription}
			</p>
			
			<p>
				<label><fmt:message key="newquestion.tags"/></label>
				${information.tagsAsString}
			</p>
			
			<input type="submit" value='<fmt:message key="moderation.accept" />' />
	
		</form>
	</div>
</c:forEach>

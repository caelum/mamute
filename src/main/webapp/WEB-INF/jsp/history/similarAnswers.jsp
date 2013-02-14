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

	<div class="history-form hidden">
		<form method="post" class="moderate-form" action="${linkTo[HistoryController].publish}${information.answer.typeName}">
			<h2>
				${information.author.name} às
				<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
					time="${information.createdAt}"></tags:jodaTime>
			</h2>
			<p>
				<fmt:message key="newquestion.title"/>:
				${information.answer.question.title}
			</p>
			<p>
				${information.markedDescription}
			</p>
			
			<p>
				${information.comment}
			</p>
				
			
			<input type="hidden" name="moderatableId" value="${information.answer.id}"/>
			<input type="hidden" name="aprovedInformationId" value="${information.id}"/>
			<input type="hidden" name="aprovedInformationType" value="AnswerInformation"/>			

			<input type="submit" value='<fmt:message key="moderation.accept" />' />
	
		</form>
	</div>
</c:forEach>

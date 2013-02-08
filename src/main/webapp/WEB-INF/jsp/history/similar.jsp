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
		<form method="post" class="history-form" action="${linkTo[HistoryController].publish[information.question.id][information.id]}">
			<h4>
				${information.author.name} às
				<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
					time="${information.createdAt}"></tags:jodaTime>
			</h4>
			
			<label><fmt:message key="newquestion.title"/><input value="${information.title}" /></label>
			<textarea rows="20" cols="">${information.description}</textarea>
			
			<label for="tags"><fmt:message key="newquestion.tags"/></label>	
			<input id="tags" type="text" name="tagNames"
				class="hintable autocomplete" value="${information.tagsAsString}"
				data-hint-id="newquestion-tags-hint"
				data-autocomplete-id="newquestion-tags-autocomplete" />
			<ul class="tags autocompleted-tags" id="newquestion-tags-autocomplete"></ul>
			
			<input type="submit" value='<fmt:message key="history.submit" />' />
	
		</form>
		<form method="post" action="${linkTo[HistoryController].refuse[information.id]}">
			<input type="submit" value='<fmt:message key="history.refuse" />' />
		</form>
	</div>
</c:forEach>

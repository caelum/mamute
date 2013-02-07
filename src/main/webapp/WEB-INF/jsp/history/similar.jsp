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

	<form method="post" class="hidden">
		<h4>
			${information.author.name} às
			<tags:jodaTime pattern="DD-MM-YYYY HH:mm"
				time="${information.createdAt}"></tags:jodaTime>
		</h4>
		<textarea rows="20" cols="">${information.description}</textarea>
		
		<input id="tags" type="text" name="tagNames"
			class="hintable autocomplete" value="${information.tagsAsString}"
			data-hint-id="newquestion-tags-hint"
			data-autocomplete-id="newquestion-tags-autocomplete" />
			
		<ul class="tags autocompleted-tags" id="newquestion-tags-autocomplete"></ul>

	</form>

</c:forEach>

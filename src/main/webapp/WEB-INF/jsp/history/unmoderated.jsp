<ul class="pending-questions">
	<c:forEach var="entry" items="${pendingQuestions.entrySet}">
		<li>
			<a href="${linkTo[QuestionController].showQuestion[entry.key.id][entry.key.sluggedTitle]}">
				${entry.key.title}
			</a>
			- <a href="${linkTo[HistoryController].similar[entry.key.id]}">Moderar</a>
		</li>
			<ul>
				<c:forEach var="information" items="${entry.value}">
					<li>
						<tags:jodaTime pattern="DD-MM-YYYY HH:mm" time="${information.createdAt}"></tags:jodaTime>
						 - by ${information.author}
					</li>
				</c:forEach>
			</ul>
	</c:forEach>
	
	<c:forEach var="entry" items="${pendingAnswers.entrySet}">
		<li>
			<a href="${linkTo[QuestionController].showQuestion[entry.key.question.id][entry.key.question.sluggedTitle]}">
				${entry.key.question.title} (resposta)
			</a>
			- <a href="${linkTo[HistoryController].similarAnswers[entry.key.id]}">Moderar</a>
		</li>
			<ul>
				<c:forEach var="information" items="${entry.value}">
					<li>
						<tags:jodaTime pattern="DD-MM-YYYY HH:mm" time="${information.createdAt}"></tags:jodaTime>
						 - by ${information.author}
					</li>
				</c:forEach>
			</ul>
	</c:forEach>
	
</ul>
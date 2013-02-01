<form action="${linkTo[AnswerController].newAnswer[question]}" method="post" class="answerForm" >
	<label><fmt:message key="newanswer.answer"/>
	<textarea name="answer.text" data-hint-id="newanswer-answer-hint" class="required hintable" minlength="15"></textarea>
	</label>
	<input type="submit" />

	<div id="newanswer-answer-hint" class="hint"><fmt:message key="newanswer.answer.hint" /></div>
</form>

<form action="${linkTo[AnswerController].newAnswer[question]}" method="post" class="answerForm" >
	<label><fmt:message key="answer.text"/></label>
	<textarea name="answer.text" class="required" minlength="15"></textarea>
	<input type="submit" />
</form>

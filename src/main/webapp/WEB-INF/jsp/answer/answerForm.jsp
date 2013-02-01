<form  action="${linkTo[AnswerController].newAnswer[question]}" method="post" >
	<label><fmt:message key="answer.text"/></label>
	<textarea name="answer.text"></textarea>
	<input type="submit" />
</form>

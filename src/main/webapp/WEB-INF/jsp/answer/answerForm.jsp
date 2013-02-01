<form action="${linkTo[AnswerController].newAnswer[question]}"
	method="post" class="validated-form">

	<div class="wmd">
		<div class="wmd-panel">
			<div id="wmd-button-bar"></div>
			<textarea class="required wmd-input" id="wmd-input"
				minlength="30" name="answerText"></textarea>
		</div>
		<div id="wmd-preview" class="wmd-panel wmd-preview"></div>
		
	</div>

	<input type="submit" />

</form>
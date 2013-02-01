
<form action="${linkTo[SignupController].signup}" method="POST" class="validated-form">
	<input name="name" class="required"/>
	<input name="email" class="required"/>
	<input name="password" type="password" class="required"/>
	<input type="submit"/>
</form>
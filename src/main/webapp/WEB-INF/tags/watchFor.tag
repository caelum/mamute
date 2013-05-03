<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<div>
	<a class="watch requires-login" href="${linkTo[QuestionController].watch[question.id]}">
		<span class="icon-eye-open icon-2x container ${!isWatching ? 'icon-muted' : '' }"></span>
	</a>
</div>

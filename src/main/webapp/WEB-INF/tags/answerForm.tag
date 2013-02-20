<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String"%>
<%@attribute name="edit" required="false" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<h2 class="title page-title"><fmt:message key="newanswer.answer.your_answer"/></h2>
<form action="${uri}" method="post" class="validated-form hinted-form">
	<tags:markDown value="${answer.description}" hintId="newanswer-answer-hint" />
	
	<c:if test='${not empty edit}'>
		<label for="comment"><fmt:message key="answer.form.comment" /></label>
		<input type="text" data-hint-id="answer-comment-hint" class="required hintable" name="comment" />
	</c:if>
	
	<input class="post-submit big-submit" value="<fmt:message key="newanswer.answer.submit"/>" type="submit" />
</form>

<div class="form-hints">
	<div id="newanswer-answer-hint" class="hint">
		<h2 class="title page-title"><fmt:message key="newanswer.answer.your_answer"/></h2>
		<p><fmt:message key="newanswer.answer.hint" /></p>
	</div>
	
	<c:if test='${not empty edit}'>
		<div id="answer-comment-hint" class="hint">
			<p><fmt:message key="answer.comment.hint"/></p>
		</div>
	</c:if>
</div>

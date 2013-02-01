<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="add-a-comment">
	<a href="#"><fmt:message key="add_comment" /></a>
	<div>
		<form action="<c:url value="/comments/add"/>" class="ajax">
			<textarea class="required " minlength="15" name="comment.text"></textarea>
			<input type="submit"
				value="<fmt:message key="comment.add_comment_submit"/>" />
		</form>
		<div><fmt:message key="comment.text.hint"/></div>
	</div>
</div>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.Commentable" required="true" %>
<ul>
<c:forEach var="comment" items="${item.comments }">
	<li>${comment.htmlComment } by ${comment.author.name }</li>
</c:forEach>
</ul>
<div class="edit-via-ajax">
	<a href="#"><fmt:message key="comment.add_comment" /></a>
	<div>
		<form action="<c:url value="/${item.typeName }/${item.id }/comment"/>" class="validated-form ajax">
			<textarea class="required to-focus" minlength="15" name="message"></textarea>
			<input type="submit"
				value="<fmt:message key="comment.add_comment_submit"/>" />
		</form>
		<div><fmt:message key="comment.text.hint"/></div>
	</div>
</div>

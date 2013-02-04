<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.Commentable" required="true" %>
<c:set var="ajaxResultName" value="new-comment-for-${item.typeName}-new-comment-${item.id}"/>

<ul id="${ajaxResultName }">
<c:forEach var="comment" items="${item.comments }">
	<li>
	<span id="comment-${comment.id }">${comment.htmlComment }</span> by ${comment.author.name }
	<tags:editFor item="${comment}" field="comment" value="${comment.comment }" ajaxResult="comment-${comment.id}" />
	</li>
</c:forEach>
</ul>

<span class="edit-via-ajax">
	<a href="#" class='requires-login'><fmt:message key="comment.add_comment" /></a>
	<span>
		<form action="<c:url value="/${item.typeName }/${item.id }/comment"/>" class="validated-form ajax" data-ajax-result="${ajaxResultName }" data-ajax-on-callback="append">
			<textarea class="required to-focus hintable" minlength="15" name="message" data-hint-id="${ajaxResultName }-hint"></textarea>
			<input type="submit"
				value="<fmt:message key="comment.add_comment_submit"/>" />
		</form>
		<span id="${ajaxResultName }-hint"><fmt:message key="comment.text.hint"/></span>
	</span>
</span>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="item" type="br.com.caelum.brutal.model.Comment" required="true" %>
<%@attribute name="field" type="java.lang.String" required="true" %>
<%@attribute name="value" required="true" %>
<%@attribute name="ajaxResult" required="true" %>
<c:set var="editKey" value="${not empty currentUser and item.author.id eq currentUser.id ? 'edit' : 'suggest_edition'}" />
<div class="edit-via-ajax">
	<a class="requires-login requires-karma" data-author="${item.author.id eq currentUser.id }" href="#"><fmt:message key="${editKey}"/></a>
	<div class="edit-form hidden">
		<form action="<c:url value="/comentario/editar/${item.id}"/>" class="validated-form ajax hinted-form" data-ajax-result="${ajaxResult }">
			<textarea class="required to-focus hintable text-input" minlength="15" name="${field}" data-hint-id="${ajaxResult }-hint">${value }</textarea>
			<input class="post-submit" type="submit"
				value="<fmt:message key="${editKey}_form.submit"/>" />
		</form>
		<div class="form-hints">
			<div class="hint" id="${ajaxResult}-hint"><fmt:message key="${field}.hint"/></div>
		</div>
	</div>
</div>
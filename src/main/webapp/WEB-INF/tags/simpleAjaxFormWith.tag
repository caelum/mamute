<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="field" type="java.lang.String" required="true" %>
<%@attribute name="value" required="false" %>
<%@attribute name="callbackTarget" type="java.lang.String" required="true" %>
<%@attribute name="action" type="java.lang.String" required="true" %>
<%@attribute name="submit" type="java.lang.String" required="true" %>
<%@attribute name="onCallback" type="java.lang.String" required="false" %>
<div class="simple-ajax-form">
	<jsp:doBody/>
	<div class="ajax-form hidden">
		<form action="${action}" class="validated-form ajax" data-ajax-on-callback="${onCallback}" data-ajax-result="${callbackTarget}">
			<textarea class="required to-focus hintable text-input" 
			minlength="15" maxlength="600" name="${field}" 
			data-hint-id="${callbackTarget}-hint"><c:out value="${value}" escapeXml="true"/></textarea>
			<input class="post-submit" type="submit" value="${submit}"/>
		</form>
		<div class="hint" id="${callbackTarget}-hint"><fmt:message key="${field}.hint"/></div>
	</div>
</div>

<!-- <div class="edit-via-ajax"> -->
<!-- 	<div class="post-container comment-container"> -->
<!-- 		<form action=""  -->
<%-- 			class="validated-form ajax" data-ajax-result="${ajaxResultName}"  --%>
<!-- 			data-ajax-on-callback="append"> -->
<%-- 			<label for="comment"><fmt:message key="comment.label" /></label> --%>
<%-- 			<textarea id="comment" class="text-input required to-focus hintable comment-textarea" minlength="15" name="message" data-hint-id="${ajaxResultName}-hint"></textarea> --%>
<%-- 			<input type="submit" class="post-submit comment-submit" value="<fmt:message key="comment.submit"/>" /> --%>
<!-- 		</form> -->
<!-- 	</div> -->
<%-- 	<div class="hint" id="${ajaxResultName}-hint"><fmt:message key="comment.hint"/></div> --%>
<!-- </div> -->

<!-- <div class="edit-via-ajax"> -->
<%-- 	<a class="requires-login requires-karma" data-author="${item.author.id eq currentUser.id }" href="#"><fmt:message key="${editKey}"/></a> --%>
<!-- 	<div class="edit-form hidden"> -->
<%-- 		<form action="<c:url value="/comentario/editar/${item.id}"/>" class="validated-form ajax" data-ajax-on-callback="${onCallback}" data-ajax-result="${ajaxResult }"> --%>
<%-- 			<textarea class="required to-focus hintable text-input" minlength="15" name="${field}" data-hint-id="${ajaxResult }-hint"> --%>
<%-- 				<c:out value="${value}" escapeXml="true"/> --%>
<!-- 			</textarea> -->
<!-- 			<input class="post-submit" type="submit" -->
<%-- 				value="<fmt:message key="${editKey}_form.submit"/>" /> --%>
<!-- 		</form> -->
<%-- 		<div class="hint" id="${ajaxResult}-hint"><fmt:message key="${field}.hint"/></div> --%>
<!-- 	</div> -->
<!-- </div> -->
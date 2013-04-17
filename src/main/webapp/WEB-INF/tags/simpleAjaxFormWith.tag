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
			<input class="post-submit submit" type="submit" value="${submit}"/>
			<button class="post-submit  submit cancel "><fmt:message key="cancel_button"/></button>
		</form>
		<div class="hint" id="${callbackTarget}-hint"><fmt:message key="${field}.hint"/></div>
	</div>
</div>

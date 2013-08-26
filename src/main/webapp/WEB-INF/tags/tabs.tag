<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="titleKey" type="java.lang.String" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="subheader subheader-with-tab">
	<c:if test="${not empty titleKey}">
		<h2 class="title page-title">
			<fmt:message key="${titleKey}"/>
		</h2>
	</c:if>
	<div class="${not empty titleKey ? 'tabs' : 'tabs tabs-tag'}">
		<jsp:doBody />
	</div>
</div>
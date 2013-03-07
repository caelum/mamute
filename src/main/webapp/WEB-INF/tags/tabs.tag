<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="titleKey" type="java.lang.String" required="true" %>

<div class="subheader subheader-with-tab">
	<h2 class="title page-title">
		<fmt:message key="${titleKey}"/>
	</h2>
	<div class="tabs">
		<jsp:doBody />
	</div>
</div>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="titleKey" type="java.lang.String" required="false" %>
<%@attribute name="title" type="java.lang.String" required="false" %>
<%@attribute name="useSubheader" type="java.lang.Boolean" required="false"%>

<div class="${useSubheader?'subheader':''} subheader-with-tab ${titleKey eq 'moderation' ? 'subheader' : ''}">
	<c:if test="${not empty titleKey}">
		<h2 class="title page-title">
			${t[titleKey]}
		</h2>
	</c:if>
	<c:if test="${not empty title}">
		<h2 class="title page-title">
			${title}
		</h2>
	</c:if>
	<div class="${not empty titleKey || not empty title ? 'tabs' : 'tabs tabs-tag'}">
		<jsp:doBody />
	</div>
</div>
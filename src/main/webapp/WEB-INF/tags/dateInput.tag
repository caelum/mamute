<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="name" required="true" type="java.lang.String" description="The name of the input field" %>
<%@attribute name="id" required="true" type="java.lang.String" description="The ID of the input field" %>
<%@attribute name="cssClass" required="false" type="java.lang.String" description="The CSS class (usually 'date')" %>
<%@attribute name="value" required="false" type="org.joda.time.base.AbstractInstant" description="The value which is pre-set" %>

<c:set var="dateFormat" value="${t['date.joda.simple.pattern']}" />
<input type="text" name="${name}" id="${id}"
	class="text-input date ${cssClass}"
	maxlength="10"
	placeholder="<c:out value="${fn:toLowerCase(dateFormat)}" escapeXml="true" />"
	data-dateformat="<c:out value="${dateFormat}" escapeXml="true" />"
	value="<tags:jodaTime pattern="${dateFormat}" time="${value}"/>" />

<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="histories" type="java.util.List" required="true" %>

<h2 class="history-title page-title title"><tags:pluralize key="moderation.version" count="${histories.size()}"/> (${histories.size()}):</h2>
<select class="history-select-version">
	<c:forEach items="${histories}" var="information" varStatus="status">
		<c:set var="formattedDate">
			<tags:jodaTime pattern="dd-MM-YYYY" time="${information.createdAt}" />
		</c:set>
		<c:set var="formattedTime">
			<tags:jodaTime pattern="HH:mm" time="${information.createdAt}" />
		</c:set>

		<option ${status.index == 0 ? 'selected' : '' } value="${status.index}">
			${t["moderation.specific_version_select"].args(information.author.name, fn:trim(formattedDate), fn:trim(formattedTime))}
		</option>
	</c:forEach>
</select>

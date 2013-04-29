<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="histories" type="java.util.List" required="true" %>

<h2 class="history-title page-title title"><tags:pluralize key="moderation.version" count="${histories.size()}"/>(${histories.size()}):</h2>
<select class="history-select-version">
	<c:forEach items="${histories}" var="information" varStatus="status">
		<option ${status.index == 0 ? 'selected' : '' } value="${status.index}">
			${information.author.name} às
			<tags:jodaTime pattern="dd-MM-YYYY HH:mm"
				time="${information.createdAt}"></tags:jodaTime>
		</option>
	</c:forEach>
</select>

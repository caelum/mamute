<table>
	<c:forEach var="history" items="${histories }">
		<tr>
			<td>${history.objectType}</td>
			<td>${history.fieldName}</td>
			<td><fmt:formatDate value="${history.createdAt.time}"/></td>
			<td><a class="iframe-load"
				href="<c:url value="/history/${history.id }/similar" />">load</a></td>
		</tr>
	</c:forEach>
</table>
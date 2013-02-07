<table>
	<c:forEach var="history" items="${histories}">
		<tr>
			<td><a href=>${history.title}</a></td>
			<td><tags:jodaTime pattern="DD-MM-YYYY" time="${history.createdAt}"></tags:jodaTime></td>
		</tr>
	</c:forEach>
</table>
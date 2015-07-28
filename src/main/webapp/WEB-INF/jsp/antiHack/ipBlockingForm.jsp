<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle}" />
<div>
	<form method="post" action="${linkTo[AntiHackController].newBlockedIp}">
		<label for="ip">
			${t['ipblocking.label']}
		</label>
		<input id="ip" name="blockedIp.ip" class="text-input" >
		<button class="post-submit big-submit submit ignore">${t['ipblocking.submit']}</button>
	</form>
</div>

<h2 class="title page-title subheader">
	${t['ipblocking.blocked.ips']}
</h2>

<table>
	<tr>
		<th>${t['ipblocking.expression']}</th>
		<th>${t['ipblocking.author']}</th>
		<th></th>
	</tr>
	<c:forEach var="blockedIp" items="${blockedIps}">
		<tr>
			<td>${blockedIp.ip}</td>
			<td>${blockedIp.author.name}</td>
			<td>
				<a href="#" class="unblock">${t['ipblocking.unblock']}</a>
				<form method="post" action="${linkTo[AntiHackController].newBlockedIp()}/${blockedIp.id}">
					<input type="hidden" name="_method" value="DELETE">
				</form>
			</td>
		</tr>
	</c:forEach>
</table>


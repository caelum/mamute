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
<ul>
	<c:forEach var="blockedIp" items="${blockedIps}">
		<li> ${blockedIp.ip}  &mdash;
			<a href="#" class="unblock">${t['ipblocking.unblock']}</a>
			<form method="post" action="${linkTo[AntiHackController].newBlockedIp()}/${blockedIp.id}">
				<input type="hidden" name="_method" value="DELETE">
			</form>
	</c:forEach>
</ul>


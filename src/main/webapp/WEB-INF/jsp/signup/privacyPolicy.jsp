<c:set var="title" value="${t['metas.privacy_policy.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<h2 class="title big-text-title">${t['privacy_policy.introduction.title']}</h2>
<div class="big-text">
	<p>${t['privacy_policy.introduction.content']}</p>
</div>

<h2 id="privacy-what-we-collect" class="title big-text-title">${t['privacy_policy.what_we_collect.title']}</h2>

<h2 class="title" id="given-information">${t['privacy_policy.what_we_collect.user_given_information.title']}</h2>
<div class="big-text">
	<p>${t['privacy_policy.what_we_collect.user_given_information.content']}</p>
</div>
<h2 class="title" id="collected-information">${t['privacy_policy.what_we_collect.site_use.title']}</h2>
<div class="big-text">
	<p>${t['privacy_policy.what_we_collect.site_use.content']}</p>
</div>


<h2 id="privacy-what-we-do" class="title big-text-title">${t['privacy_policy.what_we_do.title']}</h2>

<h2 class="title">${t['privacy_policy.what_we_do.user_given_information.title']}</h2>
<div class="big-text">
	<p>${t['privacy_policy.what_we_do.user_given_information.content']}</p>
</div>
<h2 class="title">${t['privacy_policy.what_we_do.site_use.title']}</h2>
<div class="big-text">
	<p>${t['privacy_policy.what_we_do.site_use.content']}</p>
</div>


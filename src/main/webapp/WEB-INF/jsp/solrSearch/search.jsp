<c:set var="title" value="${t['metas.search.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div id="search-results">
    <section class="first-content content">
        <tags:questionList recentTags="${recentTags}" questions="${results}" title="Results for '${query}'" tabs="${tabs}"/>
    </section>
</div>
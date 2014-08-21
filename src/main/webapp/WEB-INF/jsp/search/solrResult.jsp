<fmt:message key="metas.search.title" var="title"/>
<fmt:message key="site.name" var="siteName"/>

<fmt:message key="metas.generic.title" var="genericTitle">
    <fmt:param value="${siteName}"/>
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<div id="search-results">
    <section class="first-content content">
        <tags:questionList recentTags="${recentTags}" questions="${results}" title="Results for '${query}'" tabs="${tabs}"/>
    </section>
</div>
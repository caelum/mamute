<c:set var="title" value="${tagPage.tag.name}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title} - ${tag.name}"/>

<h2 class="title subheader page-title main-thread-title">${t['tag_page.form.edit.title']} ${tag.name}</h2>

<tags:tagPageForm uri="${linkTo[TagPageController].editTagPage(tagPage.tag.uriName)}" submitTextKey="tag_page.form.edit.submit"/>

<c:set var="title" value="${t['metas.tag_page.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title} - ${tag.name}"/>

<h2 class="title subheader page-title main-thread-title">${t['tag_page.form.new.title']} ${tag.name}</h2>
<tags:tagPageForm uri="${linkTo[TagPageController].newTagPage(tag.name)}" submitTextKey="tag_page.form.new.submit"/>

<fmt:message key="metas.tag_page.title" var="title"/>
<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title} - ${tag.name}"/>

<h2 class="title subheader page-title main-thread-title"><fmt:message key="tag_page.form.edit.title"/> ${tag.name}</h2>

<tags:tagPageForm uri="${linkTo[TagPageController].editTagPage(tag.name)}" submitTextKey="tag_page.form.edit.submit"/>

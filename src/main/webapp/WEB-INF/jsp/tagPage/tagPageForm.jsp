<fmt:message key="metas.tag_page.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title} - ${tag.name}"/>

<h2 class="title subheader page-title"><fmt:message key="tag_page.form.title"/> ${tag.name}</h2>
<form class="validated-form question-form hinted-form" action='${linkTo[TagPageController].newTagPage[tag.name]}' method="post" >
	<label for="tagPage-about"><fmt:message key="tag_page.form.about.label" /></label>
	<tags:markDown name="about" htmlClass="required" minlength="100"/>
	<input class="post-submit big-submit" type="submit" value="<fmt:message key="tag_page.form.submit"/>" />
</form>
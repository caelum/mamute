<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String"%>
<%@attribute name="submitTextKey" required="true" type="java.lang.String"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<form class="validated-form question-form hinted-form" action='${uri}' method="post" >
	<label for="tagPage-about">${t['tag_page.form.about.label']}</label>
	<tags:markDown name="about" htmlClass="required" value="${tagPage.about}" minlength="100"/>
	<input class="post-submit big-submit" type="submit" value="${t[submitTextKey]}" />
</form>
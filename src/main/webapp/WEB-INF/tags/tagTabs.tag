<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<%@attribute type="br.com.caelum.brutal.model.Tag" name="tag" required="false"%>
<%@attribute type="java.lang.Boolean" name="hasAbout" required="true"%>

<tags:tabs titleKey="">
	<fmt:message key="tag_list.title" />
	<a href="${linkTo[ListController].withTag[tag.uriName]}">${tag.name}</a>
	<c:if test="${hasAbout}">
		<a href="${linkTo[TagPageController].showTagPage[tag.name]}"><fmt:message key="about.link"/></a>
	</c:if>
	<a href="<c:url value='/ranking/${tag.name}'/>" title='<fmt:message key="users.ranking.tag.title"/> ${tag.name}' class="icon-award"><fmt:message key="users.ranking.tag"/></a>
</tags:tabs>
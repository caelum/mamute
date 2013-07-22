<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="recentTags" type="java.util.List" required="true"%>

<aside class="sidebar">
	<tags:homeNewsList newses="${sidebarNews}" />
	<div class="subheader">
		<h3 class="title page-title"><fmt:message key="tags.recent"/></h3>
	</div>
	<tags:recentTagsUsage tagsUsage="${recentTags}"/>
</aside>

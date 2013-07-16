<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<%@attribute name="microdata" required="false" %>
<%@attribute name="showTime" type="java.lang.Boolean" required="true" %>

<tags:completeUser user="${touchable.information.author}" edited="true" microdata="${microdata}">
	<c:if test="${showTime}">
		<time class="when" ${microdata ? 'itemprop="dateModified"' : ""} datetime="${touchable.information.createdAt}"><fmt:message key='touch.edited'/> <tags:prettyTime time="${touchable.information.createdAt}"/></time>
	</c:if>
</tags:completeUser>

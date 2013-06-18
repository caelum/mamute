<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<%@attribute name="microdata" required="false" %>
<tags:completeUser user="${touchable.information.author}" edited="true">
	<time class="when" ${microdata ? 'itemprop="dateModified"' : ""} datetime="${touchable.information.createdAt}"><fmt:message key='touch.edited'/> <tags:prettyTime time="${touchable.information.createdAt}"/></time>
</tags:completeUser>

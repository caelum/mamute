<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<tags:completeUser user="${touchable.information.author}">
	<div class="when" itemprop="dateModified"><fmt:message key='touch.edited'/> <tags:prettyTime time="${touchable.information.createdAt}"/></div>
</tags:completeUser>

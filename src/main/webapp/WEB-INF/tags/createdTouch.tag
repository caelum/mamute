<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<tags:completeUser user="${touchable.author}">
	<div class="when" itemprop="dateCreated"><fmt:message key='touch.created'/> <tags:prettyTime time="${touchable.createdAt}"/></div>
</tags:completeUser>

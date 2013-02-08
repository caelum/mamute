<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.Touchable" required="true" %>
<ul class="post-touchs">
	<li class="touch author-touch">
		<tags:completeUser user="${touchable.author}" date="${touchable.createdAt}"/>
	</li>
	<c:if test="${not empty touchable.lastTouchedBy}">
		<li class="touch">
			<tags:completeUser user="${touchable.lastTouchedBy}" date="${touchable.lastUpdatedAt}"/>
		</li>
	</c:if>
</ul>
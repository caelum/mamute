<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<ul class="post-touchs">
	<li class="touch author-touch">
		<tags:completeUser touchText="touch.created" user="${touchable.author}" date="${touchable.createdAt}"/>
	</li>
	<c:if test="${touchable.edited}">
		<c:choose>
			<c:when test="${(touchable.information.author.id == touchable.author.id)}">
				<li class="touch">
					<div class="complete-user">
						<div class="when"><fmt:message key='touch.edited'/> <tags:prettyTime time="${touchable.information.createdAt}"/></div>
					</div>
				</li>
			</c:when>
			<c:otherwise>
				<li class="touch">
					<tags:completeUser touchText="touch.edited" user="${touchable.information.author}" date="${touchable.information.createdAt}"/>
				</li>
			</c:otherwise>
		</c:choose>
	</c:if>
</ul>
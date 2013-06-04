<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<ul class="post-touchs">
	<li class="touch author-touch"  itemscope itemtype="http://schema.org/Person" itemprop="author">
		<tags:createdTouch touchable="${touchable}"/>
	</li>
	<c:if test="${touchable.edited}">
		<c:choose>
			<c:when test="${(touchable.information.author.id == touchable.author.id)}">
				<li class="touch edited-touch">
					<div class="complete-user">
						<div class="when" itemprop="dateModified"><fmt:message key='touch.edited'/> <tags:prettyTime time="${touchable.information.createdAt}"/></div>
					</div>
				</li>
			</c:when>
			<c:otherwise>
				<li class="touch edited-touch" itemscope itemtype="http://schema.org/Person" itemprop="editor">
					<tags:editedTouch touchable="${touchable}"/>
				</li>	
			</c:otherwise>
		</c:choose>
	</c:if>
</ul>
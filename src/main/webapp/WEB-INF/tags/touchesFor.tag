<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="touchable" type="org.mamute.model.interfaces.Touchable" required="true" %>
<%@attribute name="microdata" required="false" %>
<ul class="post-touchs">
	<li class="touch author-touch">
		<tags:createdTouch touchable="${touchable}" microdata="${microdata}"/>
	</li>
	<c:if test="${touchable.edited}">
		<c:choose>
			<c:when test="${(touchable.information.author.id == touchable.author.id)}">
				<li class="touch edited-touch">
					<div class="complete-user">
					<c:set var="nameClass" value="${touchable['class'].simpleName eq 'Question'}"/>
					<c:if test="${nameClass && editedLink}"> <a href="${linkTo[HistoryController].questionHistory(touchable.id)}"> </c:if>
						<time class="when" ${microdata ? 'itemprop="dateModified"' : ""} datetime="${touchable.information.createdAt}">
							${t['touch.edited']} 
							<tags:prettyTime time="${touchable.information.createdAt}"/>
						</time>
					<c:if test="${nameClass && editedLink}"></a></c:if>
					</div>
				</li>
			</c:when>
			<c:otherwise>
				<li class="touch edited-touch">
					<tags:editedTouch touchable="${touchable}" microdata="${microdata}"/>
				</li>	
			</c:otherwise>
		</c:choose>
	</c:if>
</ul>
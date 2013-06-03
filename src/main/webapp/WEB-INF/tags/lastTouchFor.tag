<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@attribute name="touchable" type="br.com.caelum.brutal.model.interfaces.Touchable" required="true" %>
<div class="last-touch">
	<c:choose>
		<c:when test="${touchable.edited}">
			<tags:editedTouch touchable="${touchable}"/>		
		</c:when>
		<c:otherwise>
			<tags:createdTouch touchable="${touchable}"/>
		</c:otherwise>
	</c:choose>
</div>
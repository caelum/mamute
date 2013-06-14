<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="question" type="br.com.caelum.brutal.model.Question" required="true" %>
<div>
	<a rel="nofollow" class="watch requires-login" href="${linkTo[QuestionController].watch[question.id]}">
		<c:choose>
			<c:when test="${isWatching}">
				<span class="icon-eye icon-2x container" 
				      title="<fmt:message key='watch.enabled.tooltip'/>">
				</span>
			</c:when>
			<c:otherwise>
				<span class="icon-eye-off icon-muted icon-2x container" 
				      title="<fmt:message key='watch.disabled.tooltip'/>">
				</span>
			</c:otherwise>
		</c:choose>
	</a>
</div>

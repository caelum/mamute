<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="buttonContent" required="true"%>

<c:if test="${env.supports('feature.facebook.login')}">
	<a href="${facebookUrl}" class="social-button-wraper">
		<p><fmt:message key="${buttonContent}.facebook_button.label" /></p>
		<span class="face-button"><fmt:message key="${buttonContent}.button.content" /></span>
	</a>
	<p class="or">&#8212; <fmt:message key="auth.or" /> &#8212;</p>
</c:if>


<c:if test="${env.supports('feature.google.login')}">
	<a href="${googleUrl}" class="social-button-wraper">
		<p><fmt:message key="${buttonContent}.google_button.label" /></p>
		<span class="google-button"><fmt:message key="${buttonContent}.button.content" /></span>
	</a>
	<p class="or">&#8212; <fmt:message key="auth.or" /> &#8212;</p>
</c:if>

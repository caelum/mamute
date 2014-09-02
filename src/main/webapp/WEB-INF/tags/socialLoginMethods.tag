<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="buttonContent" required="true"%>

<c:if test="${env.supports('feature.facebook.login')}">
	<a href="${facebookUrl}" class="social-button-wraper">
		<p>${t['${buttonContent}.facebook_button.label']}</p>
		<span class="face-button">${t['${buttonContent}.button.content']}</span>
	</a>
	<p class="or">&#8212; ${t['auth.or']} &#8212;</p>
</c:if>


<c:if test="${env.supports('feature.google.login')}">
	<a href="${googleUrl}" class="social-button-wraper">
		<p>${t['${buttonContent}.google_button.label']}</p>
		<span class="google-button">${t['${buttonContent}.button.content']}</span>
	</a>
	<p class="or">&#8212; ${t['auth.or']} &#8212;</p>
</c:if>

<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="buttonContent" required="true"%>

<c:set var="buttonContentText" value="${buttonContent}.button.content"/>

<c:if test="${env.supports('feature.facebook.login')}">
	<a href="${facebookUrl}" class="social-button-wraper">
		<c:set var="buttonContentLabel" value="${buttonContent}.facebook_button.label"/>
		<p>${t[buttonContentLabel]}</p>
		<span class="face-button">${t[buttonContentText]}</span>
	</a>
	<p class="or">&#8212; ${t['auth.or']} &#8212;</p>
</c:if>


<c:if test="${env.supports('feature.google.login')}">
	<a href="${googleUrl}" class="social-button-wraper">
		<c:set var="buttonContentLabel" value="${buttonContent}.google_button.label"/>
		<p>${t[buttonContentLabel]}</p>
		<span class="google-button">${t[buttonContentText]}</span>
	</a>
	<p class="or">&#8212; ${t['auth.or']} &#8212;</p>
</c:if>

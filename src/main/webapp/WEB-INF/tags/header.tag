<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="title" type="java.lang.String" required="true"%>
<%@attribute name="description" type="java.lang.String" required="false"%>
<%@attribute name="facebookMetas" type="java.lang.Boolean" required="false"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" escapeXml="true" /></title>

<tags:brutal-include value="metas" />

<c:if test="${environment.name != 'production'}" >
	<link rel="stylesheet" href="<c:url value="/css/reset.css"/>">
	
	<link rel="stylesheet" href="<c:url value="/css/deps/fontello.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/fontello-codes.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/fontello-embedded.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/fontello-ie7-codes.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/fontello-ie7.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/pagedown.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/prettify.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/joyride-2.0.3.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/mamute.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/responsive-mamute.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/custom.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/pickadate/classic.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/deps/pickadate/classic.date.css"/>">
</c:if>
<c:if test="${environment.name == 'production'}" >
	<link rel="stylesheet" href="<c:url value="/css/all-${deployTimestamp}.css"/>">
</c:if>

<!--[if lt IE 9]>
	<script src="<c:url value="/js/deps/html5shiv.js"/>"></script>
<![endif]-->

<!--[if lte IE 8]>
	<script type="text/javascript">
		var htmlshim='abbr,article,aside,audio,canvas,details,figcaption,figure,footer,header,mark,meter,nav,output,progress,section,summary,time,video'.split(',');
		var htmlshimtotal=htmlshim.length;
		for(var i=0;i<htmlshimtotal;i++) document.createElement(htmlshim[i]);
	</script>
<![endif]-->

<link rel="canonical" href="${currentUrl}"/>
</head>
<body>

	<tags:brutal-include value="header" />
	
	<div class="container">
		<tags:messages messagesList="${messages}" />
		<tags:messages messagesList="${errors}" />
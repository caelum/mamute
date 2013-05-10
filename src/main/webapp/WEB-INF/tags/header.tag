<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="env" uri="http://www.caelum.com.br/vraptor-environment/taglib"%>
<%@attribute name="title" type="java.lang.String" required="true"%>
<%@attribute name="description" type="java.lang.String" required="false"%>
<%@attribute name="facebookMetas" type="java.lang.Boolean" required="false"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${title}</title>
<c:if test="${not empty description}">
	<meta name="description" content="${description}">
</c:if>
<c:if test="${not empty facebookMetas || facebookMetas == true}">
	<meta property="og:title" content="${title}">
	<meta property="og:site_name" content="guj.com.br">
	<meta property="og:url" content='${currentUrl}'>
	<meta property="og:type" content="website">
	<meta property="og:image" content="<env:get key="host"/><c:url value="/imgs/guj-logo.png"/>">
</c:if>

<link rel="stylesheet" href="<c:url value="/css/reset.css"/>">
<c:if test="${environment.name != 'production'}" >
	<link rel="stylesheet" href="<c:url value="/css/font-awesome.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/pagedown.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/prettify.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/joyride-2.0.3.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/brutal.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/responsive-brutal.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/jquery-ui-1.10.2.custom.css"/>">
</c:if>
<c:if test="${environment.name == 'production'}" >
	<link rel="stylesheet" href="<c:url value="/css/all-deps${deployTimestamp}.css"/>">
	<link rel="stylesheet" href="<c:url value="/css/all-brutal${deployTimestamp}.css"/>">
</c:if>
<!--[if lt IE 9]>
	<script src="<c:url value="/js/html5shiv.js"/>"></script>
<![endif]-->
</head>
<body>

	<tags:brutal-include value="header.jspf" />
	
	<div class="container">
		<tags:messages messagesList="${messages}" />
		<tags:messages messagesList="${errors}" />
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

<c:set var="metaDescription" value="${description}" scope="request" />
<c:set var="facebookMetas" value="${facebookMetas}" scope="request" />
<tags:brutal-include value="metas" />

<!-- build:css /css/mamute/mamute.css -->
<link rel="stylesheet" href="/css/mamute/reset.css">

<link rel="stylesheet" href="/css/deps/fontello.css">
<link rel="stylesheet" href="/css/deps/fontello-codes.css">
<link rel="stylesheet" href="/css/deps/fontello-embedded.css">
<link rel="stylesheet" href="/css/deps/fontello-ie7-codes.css">
<link rel="stylesheet" href="/css/deps/fontello-ie7.css">
<link rel="stylesheet" href="/css/deps/pagedown.css">
<link rel="stylesheet" href="/css/deps/prettify.css">
<link rel="stylesheet" href="/css/deps/joyride-2.0.3.css">

<link rel="stylesheet" href="/css/mamute/404.css">
<link rel="stylesheet" href="/css/mamute/about.css">
<link rel="stylesheet" href="/css/mamute/ads.css">
<link rel="stylesheet" href="/css/mamute/answer.css">
<link rel="stylesheet" href="/css/mamute/button.css">
<link rel="stylesheet" href="/css/mamute/comments.css">
<link rel="stylesheet" href="/css/mamute/footer.css">
<link rel="stylesheet" href="/css/mamute/form.css">
<link rel="stylesheet" href="/css/mamute/generics.css">
<link rel="stylesheet" href="/css/mamute/icons.css">
<link rel="stylesheet" href="/css/mamute/list-question.css">
<link rel="stylesheet" href="/css/mamute/main-header.css">
<link rel="stylesheet" href="/css/mamute/main-menu.css">
<link rel="stylesheet" href="/css/mamute/main-tags.css">
<link rel="stylesheet" href="/css/mamute/mamute-joyride.css">
<link rel="stylesheet" href="/css/mamute/markdown.css">
<link rel="stylesheet" href="/css/mamute/messages.css">
<link rel="stylesheet" href="/css/mamute/moderator.css">
<link rel="stylesheet" href="/css/mamute/news.css">
<link rel="stylesheet" href="/css/mamute/pagination.css">
<link rel="stylesheet" href="/css/mamute/ranking.css">
<link rel="stylesheet" href="/css/mamute/second-header.css">
<link rel="stylesheet" href="/css/mamute/show-question.css">
<link rel="stylesheet" href="/css/mamute/sidebar.css">
<link rel="stylesheet" href="/css/mamute/subheader.css">
<link rel="stylesheet" href="/css/mamute/tags.css">
<link rel="stylesheet" href="/css/mamute/text.css">
<link rel="stylesheet" href="/css/mamute/user.css">
<link rel="stylesheet" href="/css/mamute/vote.css">
<link rel="stylesheet" href="/css/mamute/default.css">

<link rel="stylesheet" href="/css/deps/pickadate/classic.css">
<link rel="stylesheet" href="/css/deps/pickadate/classic.date.css">
<!-- endbuild -->

<link rel="stylesheet" href="${contextPath}/css/deps/custom.css">

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

<tags:brutal-include value="headJavascripts" />

<link rel="canonical" href="${currentUrl}"/>
</head>
<body>

	<tags:brutal-include value="header" />

	<div class="container">
		<tags:messages messagesList="${mamuteMessages}" />
		<tags:messages messagesList="${errors}" />
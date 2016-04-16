<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@attribute name="title" type="java.lang.String" required="true"%>
<%@attribute name="description" type="java.lang.String" required="false"%>
<%@attribute name="facebookMetas" type="java.lang.Boolean" required="false"%>

<!DOCTYPE html>
<html lang="${env.get('locale')}">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title><c:out value="${title}" escapeXml="true" /></title>

<c:set var="metaDescription" value="${description}" scope="request" />
<c:set var="facebookMetas" value="${facebookMetas}" scope="request" />
<tags:brutal-include value="metas" />

<!-- build:css /css/mamute.css -->
<link rel="stylesheet" href="/assets/generated-css/reset.css">

<link rel="stylesheet" href="/css/deps/fontello.css">
<link rel="stylesheet" href="/css/deps/fontello-codes.css">
<link rel="stylesheet" href="/css/deps/fontello-embedded.css">
<link rel="stylesheet" href="/css/deps/fontello-ie7-codes.css">
<link rel="stylesheet" href="/css/deps/fontello-ie7.css">
<link rel="stylesheet" href="/css/deps/pagedown.css">
<link rel="stylesheet" href="/css/deps/prettify.css">
<link rel="stylesheet" href="/css/deps/joyride-2.0.3.css">
<link rel="stylesheet" href="/css/deps/select2.css">

<link rel="stylesheet" href="/assets/generated-css/404.css">
<link rel="stylesheet" href="/assets/generated-css/about.css">
<link rel="stylesheet" href="/assets/generated-css/ads.css">
<link rel="stylesheet" href="/assets/generated-css/answer.css">
<link rel="stylesheet" href="/assets/generated-css/button.css">
<link rel="stylesheet" href="/assets/generated-css/comments.css">
<link rel="stylesheet" href="/assets/generated-css/footer.css">
<link rel="stylesheet" href="/assets/generated-css/form.css">
<link rel="stylesheet" href="/assets/generated-css/generics.css">
<link rel="stylesheet" href="/assets/generated-css/icons.css">
<link rel="stylesheet" href="/assets/generated-css/list-question.css">
<link rel="stylesheet" href="/assets/generated-css/main-header.css">
<link rel="stylesheet" href="/assets/generated-css/main-menu.css">
<link rel="stylesheet" href="/assets/generated-css/main-tags.css">
<link rel="stylesheet" href="/assets/generated-css/mamute-joyride.css">
<link rel="stylesheet" href="/assets/generated-css/markdown.css">
<link rel="stylesheet" href="/assets/generated-css/messages.css">
<link rel="stylesheet" href="/assets/generated-css/moderator.css">
<link rel="stylesheet" href="/assets/generated-css/news.css">
<link rel="stylesheet" href="/assets/generated-css/pagination.css">
<link rel="stylesheet" href="/assets/generated-css/ranking.css">
<link rel="stylesheet" href="/assets/generated-css/second-header.css">
<link rel="stylesheet" href="/assets/generated-css/show-question.css">
<link rel="stylesheet" href="/assets/generated-css/sidebar.css">
<link rel="stylesheet" href="/assets/generated-css/subheader.css">
<link rel="stylesheet" href="/assets/generated-css/tags.css">
<link rel="stylesheet" href="/assets/generated-css/text.css">
<link rel="stylesheet" href="/assets/generated-css/user.css">
<link rel="stylesheet" href="/assets/generated-css/vote.css">
<link rel="stylesheet" href="/assets/generated-css/default.css">
<link rel="stylesheet" href="/assets/generated-css/uploader.css">

<link rel="stylesheet" href="/css/deps/pickadate/classic.css">
<link rel="stylesheet" href="/css/deps/pickadate/classic.date.css">
<!-- endbuild -->

<link rel="stylesheet" href="${contextPath}/css/deps/custom.css">

<!--[if lt IE 9]>
	<script src="<c:url value="/js/html5shiv.js"/>"></script>
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
	<tags:brutal-include value="bodyTopJavascripts" />

	<tags:brutal-include value="header" />

	<div class="container">
		<tags:messages messagesList="${loginRequiredMessages}" />
		<tags:messages messagesList="${mamuteMessages}" />
		<tags:messages messagesList="${errors}" />
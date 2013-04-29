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
	<meta property="og:url" content='<env:get key="host"/><env:get key="home.url"/>'>
	<meta property="og:type" content="forum">
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
	<header class="header clear">
		<section class="second-header">
			<div class="container">
				<a class="logo big-logo sprite"
					href="${linkTo[ListController].home}">GUJ</a>
				<nav class="nav main-nav piped-nav">
					<ul class="nav-items">
						<li class="nav-item"><a class="button" href="${linkTo[ListController].home}">
							<fmt:message key="menu.questions"/></a></li>
						<li class="nav-item"><a class="button"
							href="${linkTo[ListController].listTags}"><fmt:message
									key="menu.tags" /></a></li>
						<li class="nav-item"><a class="button unanswered"
							href="${linkTo[ListController].unanswered}"><fmt:message
									key="menu.unanswered" /></a></li>
						<li class="ask nav-item"><a class="button ask-a-question"
							href='${linkTo[QuestionController].newQuestion}'><fmt:message
									key="menu.question.ask" /></a></li>
					</ul>
				</nav>
			</div>
		</section>

		<section class="first-header">
			<div class="container">
				<section class="user-area">
					<nav class="nav">
						<ul class="nav-items">
							<c:if test="${empty currentUser }">
								<li class="nav-item user-item"><a class="login"
									href="${linkTo[AuthController].loginForm[currentUrl]}?redirectUrl=${currentUrl}">
										<fmt:message key="auth.login_form_link" />
								</a></li>
								<li class="nav-item"><a class="signup"
									href="${linkTo[SignupController].signupForm}"><fmt:message
											key="signup.link" /></a></li>
							</c:if>
							<c:if test="${not empty currentUser }">
								<li class="nav-item user-item"><tags:userProfileLink
										user="${currentUser}" htmlClass="user-name" /> <span
									class="reputation">(${currentUser.karma})</span></li>
								<c:if test="${currentUser.moderator }">
									<li class="nav-item"><a
										href="${linkTo[HistoryController].history}"><fmt:message
												key="moderation.link" /> (${pendingForModeratorCount})</a></li>
								</c:if>
								<li class="nav-item"><a class="logout"
									href="${linkTo[AuthController].logout}"><fmt:message
											key="auth.logout_link" /></a></li>
							</c:if>
							<li class="nav-item"><a
								href="${linkTo[NavigationController].about}"> <fmt:message
										key="about.link" />
							</a></li>
							<c:if test="${not empty currentUser }">
								<li class="nav-item">
									<c:choose>
										<c:when test="${currentUser.karma > 12}" >
											<a class="intro" href="#"><fmt:message key="metas.intro.title" /></a>
										</c:when>
										<c:otherwise>
											<a class="intro new-users" href="#"><fmt:message key="metas.intro.new_users_title" /></a>
										</c:otherwise>
									</c:choose>
								</li>
							</c:if>
							<li class="nav-item"><a href="#"
								class="search-dropdown dropdown-trigger"
								data-target-id="dropdown-search-form">Procurar</a></li>

						</ul>
					</nav>
					<tags:searchForm/>
				</section>
			</div>
			<div class="container dropdown-target" id="dropdown-search-form">
				<tags:searchForm/>
			</div>
		</section>
	</header>

	<div class="container">
		<tags:messages messagesList="${messages}" />
		<tags:messages messagesList="${errors}" />
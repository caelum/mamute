<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="title" value="${t['metas.question.title']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<tags:newsForm uri="${linkTo[NewsController].saveEdit(news)}" news="${news}" edit="${true}" />

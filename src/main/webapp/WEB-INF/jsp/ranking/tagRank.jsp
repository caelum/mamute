<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="title" value="${t['users.ranking.tag.title']}"/>
<c:set var="askersTitle" value="${t['users.ranking.tag.askers']}"/>
<c:set var="answerersTitle" value="${t['users.ranking.tag.answerers']}"/>
<tags:header facebookMetas="${true}" title="${title}: [${tag.name}]" />

<h2 class="title page-title subheader">${title} <tags:tag tag="${tag}"/></h2>

<tags:tagRanking tagRankLastMonth="${answerersLastMonth}" tagRankAllTime="${answerersAllTime}" title="${answerersTitle}" isAskers="false"/>
<tags:tagRanking tagRankLastMonth="${askersLastMonth}" tagRankAllTime="${askersAllTime}" title="${askersTitle}" isAskers="true"/>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:message var="title" key="users.ranking.tag" />
<fmt:message var="askersTitle" key="users.ranking.tag.answerers" />
<fmt:message var="answerersTitle" key="users.ranking.tag.askers" />
<tags:header facebookMetas="${true}" title="${title}: [${tag.name}]" />

<h2 class="title page-title subheader">${title}: [${tag.name}]</h2>

<tags:tagRanking tagRankLastMonth="${answerersLastMonth}" tagRankAllTime="${answerersAllTime}" title="${answerersTitle}" isAskers="false"/>
<tags:tagRanking tagRankLastMonth="${askersLastMonth}" tagRankAllTime="${askersAllTime}" title="${askersTitle}" isAskers="true"/>
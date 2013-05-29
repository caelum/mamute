<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:message var="title" key="users.ranking.tag" />
<fmt:message var="anMTitle" key="users.ranking.tag.answerers_last_month" />
<fmt:message var="asMTitle" key="users.ranking.tag.askers_last_month" />
<fmt:message var="anATitle" key="users.ranking.tag.answerers_all_time" />
<fmt:message var="asATitle" key="users.ranking.tag.askers_all_time" />
<tags:header facebookMetas="${true}" title="${title}: [${tag.name}]" />

<h2 class="title page-title subheader">${title}: [${tag.name}]</h2>

<tags:tagRanking tagRank="${answerersLastMonth}" title="${anMTitle}" isAskers="false"/>
<tags:tagRanking tagRank="${answerersAllTime}" title="${anATitle}" isAskers="false"/>
<tags:tagRanking tagRank="${askersLastMonth}" title="${asMTitle}"isAskers="true"/>
<tags:tagRanking tagRank="${askersAllTime}" title="${asATitle}" isAskers="true"/>
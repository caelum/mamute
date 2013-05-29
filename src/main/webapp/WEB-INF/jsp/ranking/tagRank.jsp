<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tags:header facebookMetas="${true}" title="Ranking de usuários: [${tag.name}]" />

<h2 class="title page-title subheader">Ranking de usuários: [${tag.name}]</h2>

<tags:tagRanking tagRank="${answerersLastMonth}" />
<tags:tagRanking tagRank="${answerersAllTime}" />
<tags:tagRanking tagRank="${askersLastMonth}" />
<tags:tagRanking tagRank="${askersAllTime}" />
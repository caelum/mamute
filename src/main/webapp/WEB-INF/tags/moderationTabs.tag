<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated('pergunta')}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated('resposta')}"><fmt:message key="moderation.answers"/></a>
	<c:if test="${currentUser.moderator}">
		<a href="${linkTo[FlagController].topFlaggedQuestions}"><fmt:message key="moderation.flagged.questions"/></a>
		<a href="${linkTo[FlagController].topFlaggedAnswers}"><fmt:message key="moderation.flagged.answers"/></a>
		<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
	</c:if>
</tags:tabs>
<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].history()}">${t['moderation.posts']}</a>
	<c:if test="${currentUser.moderator}">
		<a href="${linkTo[FlagController].topFlagged}">${t['moderation.flagged.posts']}</a>
	</c:if>
</tags:tabs>
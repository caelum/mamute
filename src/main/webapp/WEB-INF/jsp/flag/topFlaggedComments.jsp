<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated['Question']}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated['Answer']}"><fmt:message key="moderation.answers"/></a>
	<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
</tags:tabs>

<ul>
	<c:forEach items="${flaggedComments}" var="comment">
		<li>
			${comment.comment} - ${comment.author.name}
		</li>
	</c:forEach>
</ul>
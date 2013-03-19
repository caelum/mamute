<tags:header title="metas.flagged_comments.title" description="metas.default.description"/>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated['question']}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated['answer']}"><fmt:message key="moderation.answers"/></a>
	<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
</tags:tabs>

<ul>
	<c:forEach items="${flaggedComments}" var="comment">
		<li>
			${comment.comment} - ${comment.author.name}
		</li>
	</c:forEach>
</ul>
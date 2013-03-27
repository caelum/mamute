<fmt:message key="metas.flagged_comments.title" var="title"/>
<fmt:message key="metas.generic.title" var="genericTitle" />
<tags:header title="${genericTitle} - ${title}"/>

<tags:tabs titleKey="moderation">
	<a href="${linkTo[HistoryController].unmoderated['pergunta']}"><fmt:message key="moderation.questions"/></a>
	<a href="${linkTo[HistoryController].unmoderated['resposta']}"><fmt:message key="moderation.answers"/></a>
	<a href="${linkTo[FlagController].topFlaggedComments}"><fmt:message key="moderation.flagged.comments"/></a>
</tags:tabs>

<ul>
	<c:forEach items="${flaggedComments}" var="comment">
		<li>
			${comment.comment} - ${comment.author.name}
		</li>
	</c:forEach>
</ul>
<fmt:message key="metas.unmoderated.title" var="title"/>
<fmt:message key="site.name" var="siteName" />

<fmt:message key="metas.generic.title" var="genericTitle" >
	<fmt:param value="${siteName}" />
</fmt:message>

<tags:header title="${genericTitle} - ${title}"/>

<tags:moderationTabs />

<ul class="pending-questions">
	<c:forEach var="entry" items="${pending.entrySet}">
		
		<li class="pending">
			<h3 class="title item-title">
				<c:if test="${type eq 'pergunta'}">
					<tags:similarQuestionsLinkFor question="${entry.key}"/>
					<p class="moderator-text">Quantidade de edições : ${entry.value.size()}</p>
					<tags:tagsFor taggable="${entry.key}"></tags:tagsFor>
				</c:if>
				<c:if test="${type eq 'resposta'}">
					<tags:similarQuestionsLinkFor answer="${entry.key}"/>
					<p class="moderator-text">Quantidade de edições : ${entry.value.size()}</p>
					<tags:tagsFor taggable="${entry.key.question}"></tags:tagsFor>
				</c:if>
			</h3>
			
			<div class="stats">
				<c:set var="information" value="${entry.value[fn:length(entry.value)-1]}"/>
				<span class="last-updated-at"><tags:prettyTime time="${information.createdAt}" /></span>
				<span>- último usuário a editar :</span>
				<tags:userProfileLink user="${information.author}"/>
			</div>
		</li>
	</c:forEach>
</ul>
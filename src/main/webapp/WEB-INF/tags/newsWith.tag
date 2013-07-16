<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="news" type="br.com.caelum.brutal.model.News" required="true" %>
<%@attribute name="commentVotes" type="br.com.caelum.brutal.model.CommentsAndVotes" required="true" %>

<section itemscope itemtype="http://schema.org/Article" class="post-area ${news.approved ? '' : 'highlight-post' }" >
	<c:if test="${! news.approved}">
		<fmt:message key="news.approving" /> 
	</c:if>
	<h1 itemprop="name" class="title subheader news-title"><c:out value="${news.title}" escapeXml="${true}"/></h1>
	<div class="post-meta">
		<tags:voteFor item="${news}" type="noticia" vote="${currentVote}"/>
	</div>
	<div class="post-container">
		<div class="post-text">${news.markedDescription}</div>
		<div class="post-interactions">
			<ul class="post-action-nav piped-nav nav">
				<li class="nav-item">
					<a class="post-action show-popup" href="#">
						<fmt:message key="share"/>
					</a>
					<div class="popup share small">
						<form class="validated-form">
							<label for="share-url"><fmt:message key="share.text"/></label>
							<input type="text" class="text-input required" id="share-url" value="${currentUrl}"/>
						</form>
						<a target="_blank" class="share-button" data-shareurl="http://www.facebook.com/sharer/sharer.php?u=${currentUrl}"><i class="icon-facebook-squared icon-almost-3x"></i></a>
						<a target="_blank" class="share-button" data-shareurl="https://twitter.com/share?text=${news.title}&url=${currentUrl}"><i class="icon-twitter-squared icon-almost-3x"></i></a>
						<a target="_blank" class="share-button" data-shareurl="https://plus.google.com/share?&url=${currentUrl}"><i class="icon-gplus-squared icon-almost-3x"></i></a>
						<a class="close-popup"><fmt:message key="popup.close"/></a>
					</div>
				</li>
				<li class="nav-item">
					<a class="post-action requires-login requires-karma"
					    data-author="${currentUser.current.isAuthorOf(news)}"
					    data-karma="${EDIT_NEWS}" 
					    href="<c:url value="/noticia/editar/${news.id}" />">
						<fmt:message key="edit" />
					</a>
				</li>
				<li class="nav-item">
					<c:if test="${currentUser.loggedIn && !news.alreadyFlaggedBy(currentUser.current)}">
						<a href="#" data-author="${currentUser.current.isAuthorOf(news)}" data-karma="${CREATE_FLAG}"
							data-modal-id="news-flag-modal${news.id}" 
							class="post-action author-cant requires-login flag-it requires-karma">
							<fmt:message key="flag" />
						</a>
					</c:if>
					<tags:flagItFor type="noticia" modalId="news-flag-modal${news.id}" flaggable="${news}"/>
				</li>
			</ul>
			<tags:touchesFor touchable="${news}" microdata="true"/>
		</div>
		<tags:add-a-comment groupComments="false" type="noticia" item="${news}" votes="${commentVotes}"/>
		<c:if test="${currentUser.moderator && news.hasPendingEdits()}">
			<a class="message moderator-alert" href="#"><fmt:message key="news.warns.has_edits"/></a>
		</c:if>
	</div>
</section>
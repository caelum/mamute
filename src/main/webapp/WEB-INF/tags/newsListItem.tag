<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="news" type="org.mamute.model.News" required="true" %>
<c:if test="${news.approved || currentUser.moderator}">
	<li class="post-item news-big-items ${news.approved ? '' : 'post-under-review'} ${news.isVisibleForModeratorAndNotAuthor(currentUser.current) ? 'highlight-post' : '' }">
		<div class="post-information news-information">
			<tags:postItemInformation key="post.list.vote" count="${news.voteCount}" information="votes" htmlClass="news-info"/>
		</div>
		<div class="summary news-summary">
			<div class="item-title-wrapper">
				<h3 class="title item-title news-title">
					<a href="${linkTo[NewsController].showNews(news,news.sluggedTitle)}">${news.title}</a>
				</h3>
				<div class="post-simple-information news-simple-information">
					${news.views} <tags:pluralize key="post.list.view" count="${news.views}"/>
				</div>
				<div class="comments post-simple-information news-simple-information">
					${news.getVisibleCommentsFor(currentUser.current).size()} <tags:pluralize key="post.list.comment" count="${news.getVisibleCommentsFor(currentUser.current).size()}"/>
				</div>
				<div class="summary news-description ellipsis">
					${news.getTrimmedContent()}
				</div>
			</div>
			
			<tags:lastTouchFor touchable="${news}" prettyFormat="${false}"/>
			
		</div>		
		<c:if test="${not news.approved && currentUser.moderator}">
			<a class="approve-news" href="${linkTo[NewsController].approve(news)}">${t['news.approve']}</a>
		</c:if>
	</li>
</c:if>

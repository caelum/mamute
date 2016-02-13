<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@attribute name="uri" required="true" type="java.lang.String" %>
<%@attribute name="edit" required="false" type="java.lang.Boolean" %>
<%@attribute name="news" required="false" type="org.mamute.model.News" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>

<form class="validated-form question-form hinted-form" action='${uri}' method="post" >
	<div class="news-guideline guideline">
		<h2 class="title section-title hint-title">${t['news.guideline.title']}</h2>
		${t['news.guideline.body']}
	</div>
	<label for="news-title">${t['news.title.label']}</label>
	<input id="news-title" type="text" class="required hintable text-input" 
		value="${news.title}" minlength="15" maxlength="150"
		name="title" placeholder="${t['news.title.placeholder']}" />
	
	<c:set var="descriptionPlaceholder" value="${t['news.description.placeholder']}"/>
	<tags:markDown value="${news.description}" hintId="news-description-hint"
		htmlClass="required description-input" minlength="30"/>
		
	<c:if test="${edit}">
		<label>${t['news.comment.label']}
			<input type="text" class="text-input required" minlength="5" 
				maxlength="150" name="comment" placeholder="${t['news.comment.placeholder']}" />
		</label>
	</c:if>

	<input class="post-submit big-submit" type="submit" value="${t['news.new_form.submit']}" />
</form>
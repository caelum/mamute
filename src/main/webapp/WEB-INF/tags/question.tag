<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="taggable" type="br.com.caelum.brutal.model.interfaces.Taggable" required="true" %>

<h2 class="title question-title">${taggable.title}</h2>
<div class="post-text question-description">
	${taggable.markedDescription}
</div>
<tags:tagsFor taggable="${question}"/>
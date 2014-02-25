<%@ tag language="java" pageEncoding="UTF-8"%>
<%@attribute name="tag" type="org.mamute.model.Tag" required="true" %>

<a href="${linkTo[ListController].withTag(tag.name)}" class="tag-brutal">${tag.name}</a>

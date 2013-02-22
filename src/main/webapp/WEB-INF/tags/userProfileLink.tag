<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="user" type="br.com.caelum.brutal.model.User" required="true" %>
<%@attribute name="htmlClass" type="java.lang.String" required="false" %>
<a class="${htmlClass}" href="${linkTo[UserProfileController].showProfile[user.id][user.sluggedName]}">
${user.name}
</a>

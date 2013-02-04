<form action="<c:url value="/login"/>" method="POST">
<input name="email" />
<input name="password" type="password" />
<input name="redirectUrl" value="${redirectUrl}" type="hidden" />
<input type="submit" />
</form>
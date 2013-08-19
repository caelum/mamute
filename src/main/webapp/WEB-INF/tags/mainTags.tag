<%@attribute name="tagClass" type="java.lang.String" required="false" %>
<%@attribute name="tagClassLi" type="java.lang.String" required="false" %>

<ol class="main-tags ${tagClass}">
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['java']}">java</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['android']}">android</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['c%23']}">c#</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['.net']}">.net</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['javascript']}">javascript</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['php']}">php</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['jquery']}">jquery</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['html']}">html</a></li>
	<li><a class="${tagClassLi}" href="${linkTo[ListController].withTag['sql']}">sql</a></li>
</ol>


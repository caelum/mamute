<%@attribute name="tagClass" type="java.lang.String" required="false" %>
<%@attribute name="tagClassLi" type="java.lang.String" required="false" %>
<%@attribute name="useSprite" type="java.lang.Boolean" required="false" %>

<ol class="main-tags ${tagClass}">
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-java' : '' }" href="${linkTo[ListController].withTag['java']}">java</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-android' : '' }" href="${linkTo[ListController].withTag['android']}">android</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-c' : '' }" href="${linkTo[ListController].withTag['c%23']}">c#</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-net' : '' }" href="${linkTo[ListController].withTag['.net']}">.net</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-javascript' : '' }" href="${linkTo[ListController].withTag['javascript']}">javascript</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-php' : '' }" href="${linkTo[ListController].withTag['php']}">php</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-jquery' : '' }" href="${linkTo[ListController].withTag['jquery']}">jquery</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-html' : '' }" href="${linkTo[ListController].withTag['html']}">html</a></li>
	<li class="${tagClassLi}" ><a class="${useSprite? 'main-tags-sprite main-tags-sql' : '' }" href="${linkTo[ListController].withTag['sql']}">sql</a></li>
</ol>


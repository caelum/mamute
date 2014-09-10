<c:set var="title" value="${t['metas.search.title']}"/>
<c:set var="siteName" value="${t['site.name']}"/>

<c:set var="genericTitle" value="${t['metas.generic.title'].args(siteName)}"/>

<tags:header title="${genericTitle} - ${title}"/>

<div id="search-results"></div>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("search", "1");
	var query = "${query}";
	function googleLoad() {
		var customSearchControl = new google.search.CustomSearchControl('${customGoogleSearchKey}');
		customSearchControl.draw('search-results'); 
		customSearchControl.execute(query);
	}
	google.setOnLoadCallback(googleLoad);
</script>

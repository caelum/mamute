<div id="search-results"></div>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<script type="text/javascript">
	google.load("search", "1");
	var query = "${query}";
	function googleLoad() {
		var customSearchControl = new google.search.CustomSearchControl("015657611711082820003:davabg980gk");
		customSearchControl.draw('search-results'); 
		customSearchControl.execute(query);
	}
	google.setOnLoadCallback(googleLoad);
</script>
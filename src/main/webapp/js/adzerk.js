var p="http",d="static";
if(document.location.protocol=="https:"){
	p+="s";d="engine";
}
var z=document.createElement("script");
z.type="text/javascript";
z.async=true;
z.src=p+"://"+d+".adzerk.net/ados.js";
var s=document.getElementsByTagName("script")[0];
s.parentNode.insertBefore(z,s);

var ados = ados || {};
ados.run = ados.run || [];
ados.run.push(function() {
	ados_add_placement(7120, 49043, "adzerk", 5);
	ados_load();
});


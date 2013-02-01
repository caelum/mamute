$(function(){
	$('.add-a-comment a').click(function(e) {
		e.preventDefault();
		var father = $(this).parent();
		var child = father.children();
		child.toggle();
		father.find("textarea").focus();
		return false;
	});
	$('form.ajax').submit(function(e) {
		e.preventDefault();
		return false;
	});
});
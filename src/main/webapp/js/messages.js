var Messages = (function() {
	var obj = {};
	
	obj.keys = {};
	
	obj.get = function(key) {
		if (key.length > 0) {
			if (Object.keys(obj.keys).length > 0) {
				return obj.keys[key];
			} else {
				obj.keys = JSON.parse(localStorage.messages);
				if (Object.keys(obj.keys).length > 0) {
					return obj.keys[key];
				} else {
					obj.load();
					setTimeout(function() { obj.get(key); }, 2000);
				}
			}
		}
	}
	
	obj.load = function() {
		if (localStorage.messages === undefined) {
			if (Object.keys(obj.keys).length == 0) {
				$.get('messages/loadAll', {}, function(result) {
					obj.keys = result.hashMap;
					localStorage.setItem('messages', JSON.stringify(result.hashMap));
				});
			}
		} else {
			obj.keys = JSON.parse(localStorage.messages);
		}
	}
	
	return {
		get : obj.get,
		load : obj.load
	};
	
})();


$(document).ready(function(){
	Messages.load();
});
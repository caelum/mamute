var Messages = (function() {
	var obj = {};
	
	obj.get = function(key) {
		if (key.length > 0) {
			var value =  sessionStorage.getItem(key);
			if (value != null) {
				return value;
			} else {
				return "???"+key+"???";
			}
		}
	}
	
	obj.load = function(isAsync, callback) {
		if (isAsync === undefined) {
			isAsync = true;
		}
		
		$.ajax(MESSAGES_LOADER_URL, {
			async: isAsync,
			success: function(result) {
				var keys = Object.keys(result.hashMap);
				
				for (var x = 0; x < keys.length; x++) {
					var key = keys[x];
					sessionStorage.setItem(key, result.hashMap[key]);
				}
				callback();
			}
		});
	}
	
	return {
		get : obj.get,
		load : obj.load
	};
	
})();


$(document).ready(function(){
	Messages.load(true,function(){
		$(document).trigger("messages-loaded");
	});
});
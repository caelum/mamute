package org.mamute.i18n;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

public class MessagesLoader {

	@Inject ResourceBundle bundle;
	
	
	public Map<String, String> getAllMessages(){
		Enumeration<String> keys = bundle.getKeys();
		Map<String, String> messages = new HashMap<>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			messages.put(key, bundle.getString(key));
		}
		return messages;
	}

}

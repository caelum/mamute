package org.mamute.infra.locale;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MessagesLoader {

	public Map<String, String> loadBy(String locale) {
		Properties props = new Properties();
		Map<String, String> messages = new HashMap<String, String>();
		try {
			props.load(this.getClass().getResourceAsStream("/messages.properties"));
			Set<Object> keySet = props.keySet();
			for (Object key : keySet) {
				String value = (String) props.get(key);
				messages.put((String) key, value);
			}

			if (!locale.equals("en")) {
				props.load(this.getClass().getResourceAsStream("/messages_"+locale+".properties"));
				keySet = props.keySet();
				for (Object key : keySet) {
					String value = (String) props.get(key);
					messages.put((String) key, value);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return messages;
	}

}
package org.mamute.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class AppMessages {
    
    private Properties properties;

    public AppMessages() {
        InputStream messagesStream = getClass().getResourceAsStream("/messages.properties");
        properties = new Properties();
        try {
            properties.load(messagesStream);
        } catch (IOException e) {
            throw new RuntimeException("could not load messages", e);
        }
    }

    public String getMessage(String key) {
        return properties.getProperty(key);
    }

	public String getMessage(String key, Object... params) {
		String message = properties.getProperty(key);
		message = MessageFormat.format(message, params);
		return message;
	}

}

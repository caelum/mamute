package org.mamute.vraptor.environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import br.com.caelum.vraptor.environment.ServletBasedEnvironment;

@Specializes
@ApplicationScoped
public class MamuteEnvironment extends ServletBasedEnvironment {

	private final Properties properties = new Properties();
	
	@Inject
	public MamuteEnvironment(ServletContext context) throws IOException {
		super(context);
		InputStream stream = MamuteEnvironment.class.getResourceAsStream("/mamute.properties");
		properties.load(stream);
	}
	
	/**
	 * @deprecated 
	 */
	public MamuteEnvironment() throws IOException {
	}
	
	@Override
	public boolean has(String key) {
		return super.has(key) || properties.containsKey(key);
	}
	
	@Override
	public String get(String key) {
		if (super.has(key)) {
			return resolveEnv(super.get(key));
		}
		
		if (has(key)) {
			return resolveEnv(properties.get(key).toString());
		}
			
		throw new NoSuchElementException("Key " + key + " not found in environment " + getName());
			
	}

	private String resolveEnv(String value) {
		if (value.matches("\\$\\{.+\\}")) {
			String envVar = System.getenv(value);
			if (envVar == null) {
				throw new NoSuchElementException("Environment variable " + value + " not defined!");
			}
		}
		return value;
	}
	
}

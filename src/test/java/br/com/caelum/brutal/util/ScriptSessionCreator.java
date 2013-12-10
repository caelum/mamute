package br.com.caelum.brutal.util;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;

public class ScriptSessionCreator {
    private static final Logger LOG = Logger.getLogger(ScriptSessionCreator.class);
	private SessionFactoryCreator sessionFactoryCreator; 

    public ScriptSessionCreator() {
    	Environment env = buildEnv();
    	sessionFactoryCreator = new SessionFactoryCreator(env);
    	sessionFactoryCreator.init();
	}
    
    public void dropAndCreate(){
    	sessionFactoryCreator.dropAndCreate();
    }
    
    public Session getSession() {
        SessionFactory sf = sessionFactoryCreator.getInstance();
        return sf.openSession();
    }

    private Environment buildEnv() {
        Environment env;
        try {
            String envName = System.getenv("DATAIMPORT_ENV");
            envName = envName == null? System.getProperty(ENVIRONMENT_PROPERTY) : envName; 
            envName = envName == null? "development" : envName;
            env = new DefaultEnvironment(envName);
            LOG.info("using env '" + envName + "' for script session creator");
            return env;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

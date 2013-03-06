package br.com.caelum.brutal.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

public class ScriptSessionFactoryCreator {
    private static final Logger LOG = Logger.getLogger(ScriptSessionFactoryCreator.class);
	private SessionFactoryCreator sessionFactoryCreator; 

    public ScriptSessionFactoryCreator() {
    	Environment env = buildEnv();
    	sessionFactoryCreator = new SessionFactoryCreator(env);
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
            envName = envName == null? "development" : envName;
            env = new DefaultEnvironment(envName);
            LOG.info("using env '" + envName + "' for data import");
            return env;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

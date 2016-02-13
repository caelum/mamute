package org.mamute.util;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mamute.providers.MamuteDatabaseConfiguration;
import org.mamute.providers.SessionFactoryCreator;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.EnvironmentType;

public class ScriptSessionCreator {
	
    private static final Logger LOG = Logger.getLogger(ScriptSessionCreator.class);
	private SessionFactoryCreator sessionFactoryCreator; 

    public ScriptSessionCreator() {
    	Environment env = buildEnv();
    	MamuteDatabaseConfiguration mamuteDatabaseConfiguration = new MamuteDatabaseConfiguration(env, null, null);
    	sessionFactoryCreator = new SessionFactoryCreator(mamuteDatabaseConfiguration);
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
            if (isNullOrEmpty(envName)) {
            	envName = firstNonNull(System.getProperty(ENVIRONMENT_PROPERTY), "development");
            }
            env = new DefaultEnvironment(new EnvironmentType(envName));
            LOG.info("using env '" + envName + "' for script session creator");
            return env;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

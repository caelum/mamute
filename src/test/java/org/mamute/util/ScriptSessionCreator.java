package org.mamute.util;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.EnvironmentType;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.io.IOException;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ScriptSessionCreator {
	
    private static final Logger LOG = Logger.getLogger(ScriptSessionCreator.class);
	private SessionFactoryCreator sessionFactoryCreator;

    @Inject
    private SessionFactory sessionFactory;

    public ScriptSessionCreator() {
//    	Environment env = buildEnv();
	}
    
    public void dropAndCreate() {
//    	sessionFactoryCreator.dropAndCreate();
    }
    
    public Session getSession() {
        return sessionFactory.openSession();
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

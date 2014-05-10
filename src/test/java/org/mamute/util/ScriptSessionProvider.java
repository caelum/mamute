package org.mamute.util;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.environment.EnvironmentType;
import br.com.caelum.vraptor.ioc.cdi.CDIBasedContainer;
import br.com.caelum.vraptor.test.container.CdiContainer;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.enterprise.inject.spi.CDI;
import java.io.IOException;

import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ScriptSessionProvider {

    private static final Logger LOG = Logger.getLogger(ScriptSessionProvider.class);
    public static Session session;
    private static SessionFactory factory;

    static {


    }

    public ScriptSessionProvider() {
        Environment env = buildEnv();

        CdiContainer cdiContainer = new CdiContainer();
        cdiContainer.start();
        CDIBasedContainer cdiBasedContainer = CDI.current().select(CDIBasedContainer.class).get();
        factory =  cdiBasedContainer.instanceFor(SessionFactory.class);
    }

    private static Session instance;

    public static Session getInstance() {
        return factory.openSession();
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

    public void dropAndCreate(){
//        factory.dropAndCreate();
    }
}

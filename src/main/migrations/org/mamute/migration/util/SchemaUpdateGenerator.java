package org.mamute.migration.util;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.EnvironmentType;
import br.com.caelum.vraptor.hibernate.ServiceRegistryCreator;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.mamute.providers.CustomConfigurationCreator;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;


public class SchemaUpdateGenerator {

    private static final Logger LOG = getLogger(DefaultEnvironment.class);
    private static EnvironmentType environmentType = EnvironmentType.DEVELOPMENT;

    public static Configuration getCfg() throws IOException {
        return new CustomConfigurationCreator(new DefaultEnvironment(environmentType)).getInstance();
    }

	public static void main(String[] args) throws IOException {

        Configuration configuration = getCfg();
        LOG.info("Executing SchemaUpdate. environment=" + environmentType);
        ServiceRegistry serviceRegistry = new ServiceRegistryCreator(getCfg()).getInstance();

        SchemaUpdate su = new SchemaUpdate(serviceRegistry, configuration);
        su.execute(true, false);
        LOG.info("SchemaUpdate finished.");
    }
}

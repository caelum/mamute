package org.mamute.migration.util;

import br.com.caelum.vraptor.hibernate.ConfigurationCreator;
import br.com.caelum.vraptor.hibernate.ServiceRegistryCreator;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import java.io.IOException;


public class SchemaUpdateGenerator {

    public static Configuration getCfg() {
        return new ConfigurationCreator().getInstance();
    }

	public static void main(String[] args) throws IOException {


        Configuration configuration = getCfg();
        ServiceRegistry serviceRegistry = new ServiceRegistryCreator(getCfg()).getInstance();

        br.com.caelum.vraptor.hibernate.SessionFactoryCreator sessionFactoryCreator =
                new br.com.caelum.vraptor.hibernate.SessionFactoryCreator(configuration, serviceRegistry);

        SchemaUpdate su = new SchemaUpdate(serviceRegistry, configuration);
        su.execute(true, false);
    }
}

package org.mamute.migration.util;

import static br.com.caelum.vraptor.environment.EnvironmentType.DEVELOPMENT;

import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.mamute.providers.SessionFactoryCreator;

import br.com.caelum.vraptor.environment.DefaultEnvironment;


public class SchemaUpdateGenerator {

	public static void main(String[] args) throws IOException {
		SessionFactoryCreator sessionFactoryCreator = new SessionFactoryCreator(new DefaultEnvironment(DEVELOPMENT));
		sessionFactoryCreator.init();
		Configuration cfg = sessionFactoryCreator.getCfg();
		
		SchemaUpdate su = new SchemaUpdate(cfg);
		su.execute(true, false);
	}
}

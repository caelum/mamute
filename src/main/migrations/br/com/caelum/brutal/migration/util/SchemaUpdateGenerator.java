package br.com.caelum.brutal.migration.util;

import java.io.IOException;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.vraptor.environment.DefaultEnvironment;


public class SchemaUpdateGenerator {

	public static void main(String[] args) throws IOException {
		Configuration cfg = new SessionFactoryCreator(new DefaultEnvironment("development")).getCfg();
		
		SchemaUpdate su = new SchemaUpdate(cfg);
		su.execute(true, false);
	}
}

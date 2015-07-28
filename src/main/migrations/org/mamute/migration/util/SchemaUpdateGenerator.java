package org.mamute.migration.util;

import static br.com.caelum.vraptor.environment.EnvironmentType.DEVELOPMENT;

import java.io.IOException;

import org.mamute.providers.MamuteDatabaseConfiguration;

import br.com.caelum.vraptor.environment.DefaultEnvironment;


public class SchemaUpdateGenerator {

	public static void main(String[] args) throws IOException {
		MamuteDatabaseConfiguration mamuteConfiguration = new MamuteDatabaseConfiguration(new DefaultEnvironment(DEVELOPMENT), null, null);
		mamuteConfiguration.init();
		mamuteConfiguration.getSchemaUpdate().execute(true, false);
	}

}

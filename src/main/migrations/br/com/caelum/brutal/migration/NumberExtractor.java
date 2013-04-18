package br.com.caelum.brutal.migration;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class NumberExtractor {

	public int from(SimpleMigration clazz) {
		try {
			return Integer.parseInt(clazz.getClass().getSimpleName().substring(1, 4));
		} catch (Exception e) {
			throw new BadFormedMigrationException();
		}
	}

}

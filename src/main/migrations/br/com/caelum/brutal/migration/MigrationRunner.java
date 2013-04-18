package br.com.caelum.brutal.migration;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class MigrationRunner {

	private final List<SimpleMigration> migrations;
	private final NumberExtractor extractNumber;
	private final static Logger LOGGER = LoggerFactory
			.getLogger(MigrationRunner.class);
	private final MigrationExecutor executor;
	private final Environment env;

	public MigrationRunner(List<SimpleMigration> migrations, NumberExtractor number,
			MigrationExecutor executor, Environment env) {
		this.extractNumber = number;
		this.executor = executor;
		this.env = env;
		this.migrations = sort(migrations);
	}

	private List<SimpleMigration> sort(List<SimpleMigration> unsortedMigrations) {
		Collections
				.sort(unsortedMigrations, new MigrationSorter(extractNumber));
		return unsortedMigrations;
	}

	public void execute() {
		if (env.getName().equals("testing") || env.getName().equals("acceptance")) {
			return;
		}

		prepareTables();

		for (SimpleMigration m : migrations) {
			executeMigration(m);
		}

		executor.close();
	}

	private void prepareTables() {
		try {
			executor.begin();
			executor.prepareTables();
			executor.end();
		} catch (Exception e) {
			executor.rollback();
			throw new RuntimeException("Unable to execute migration process", e);
		}
	}

	private void executeMigration(SimpleMigration m) {
		int number = extractNumber.from(m);
		if (number > executor.currentMigration()) {
			try {
				executor.begin();
				LOGGER.debug("Running migration " + number);
				executor.run(m);
				LOGGER.info("Migration " + number + " executed!");
				executor.insertNewMigration(number);
				executor.end();
			} catch (Exception e) {
				executor.rollback(m);
				throw new RuntimeException("Unable to execute migration "
						+ number, e);
			}
		}
	}

}

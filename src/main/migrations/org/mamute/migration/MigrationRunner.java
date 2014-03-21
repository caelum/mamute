package org.mamute.migration;

import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.errormail.util.StackToString;

import com.google.common.collect.Lists;

@ApplicationScoped
public class MigrationRunner {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(MigrationRunner.class);
	private List<SchemaMigration> migrations;
	private NumberExtractor extractNumber;
	private MigrationExecutor executor;
	private Environment env;

	@Deprecated
	MigrationRunner() {
	}

	@Inject
	public MigrationRunner(@Any Instance<SchemaMigration> migrations, NumberExtractor number,
			MigrationExecutor executor, Environment env) {
		this((Iterable<SchemaMigration>) migrations, number, executor, env);
	}
	
	public MigrationRunner(Iterable<SchemaMigration> migrations, NumberExtractor number,
			MigrationExecutor executor, Environment env) {
		this.extractNumber = number;
		this.executor = executor;
		this.env = env;
		this.migrations = sort(Lists.newArrayList(migrations));
	}

	private List<SchemaMigration> sort(List<SchemaMigration> unsortedMigrations) {
		Collections
				.sort(unsortedMigrations, new MigrationSorter(extractNumber));
		return unsortedMigrations;
	}

	public void execute() {
		if (env.getName().equals("testing") || env.getName().equals("acceptance")) {
			return;
		}

		prepareTables();

		for (SchemaMigration m : migrations) {
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
			LOGGER.info("Couldnt prepare tables: "+ e);
			LOGGER.info(">>>> "+ StackToString.convertStackToString(e));
			executor.rollback();
			throw new RuntimeException("Unable to execute migration process", e);
		}
	}

	private void executeMigration(SchemaMigration m) {
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

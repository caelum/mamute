package org.mamute.migration;

import static br.com.caelum.vraptor.environment.EnvironmentType.PRODUCTION;
import static java.util.Arrays.asList;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mamute.migration.MigrationExecutor;
import org.mamute.migration.MigrationRunner;
import org.mamute.migration.NumberExtractor;
import org.mamute.migration.SchemaMigration;
import org.mamute.migration.SimpleSchemaMigration;
import org.mockito.InOrder;

import br.com.caelum.vraptor.environment.DefaultEnvironment;

import javax.enterprise.inject.Vetoed;

@Vetoed
public class MigrationRunnerTest {

	private MigrationExecutor executor;
	private MigrationRunner runner;
	private SchemaMigration m1;
	private SchemaMigration m2;
	private NumberExtractor extractor;

	@Before
	public void setup() throws IOException {
		executor = mock(MigrationExecutor.class);

		m1 = mock(SchemaMigration.class);
		m2 = mock(SchemaMigration.class);

		extractor = mock(NumberExtractor.class);
		when(extractor.from(m1)).thenReturn(1);
		when(extractor.from(m2)).thenReturn(2);

		runner = new MigrationRunner(asList(m1, m2), extractor, executor, new DefaultEnvironment(PRODUCTION));
	}

	@Test
	public void should_run_all_migrations_in_ascendant_order() {
		when(executor.currentMigration()).thenReturn(0);

		runner.execute();

		InOrder inOrder = inOrder(executor);

		inOrder.verify(executor).begin();
		inOrder.verify(executor).prepareTables();
		inOrder.verify(executor).run(m1);
		inOrder.verify(executor).run(m2);
		inOrder.verify(executor).insertNewMigration(2);
		inOrder.verify(executor).end();
	}

	@Test
	public void should_rollback_if_something_fails() {
		doThrow(new RuntimeException()).when(executor).run(m2);

		try {
			runner.execute();
			fail("Should have complained");
		}catch(RuntimeException ex) {
			// ok
		}

		InOrder inOrder = inOrder(executor);

		inOrder.verify(executor).begin();
		inOrder.verify(executor).prepareTables();
		inOrder.verify(executor).end();
		inOrder.verify(executor).begin();
		inOrder.verify(executor).run(m1);
		inOrder.verify(executor).end();
		inOrder.verify(executor).begin();
		inOrder.verify(executor).run(m2);
		inOrder.verify(executor).rollback(m2);
	}

	@Test
	public void should_not_run_anything_if_no_new_migration() {
		when(executor.currentMigration()).thenReturn(2);

		runner.execute();

		verify(executor, never()).run(any(SimpleSchemaMigration.class));
	}

	@Test
	public void should_continue_from_the_last_executed() {
		when(executor.currentMigration()).thenReturn(1);

		runner.execute();

		verify(executor, never()).run(m1);
		verify(executor).run(m2);
		verify(executor).insertNewMigration(2);
	}
}

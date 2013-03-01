package br.com.caelum.brutal.migration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberExtractorTest {

	@Test
	public void should_extract_number_from_class_name() {
		int number = new NumberExtractor().from(new M001TestMigration());
		assertEquals(1, number);
	}
	
	@Test(expected=BadFormedMigrationException.class)
	public void should_throw_exception_if_name_is_incorrect() {
		new NumberExtractor().from(new NotAValidMigration());
	}
}

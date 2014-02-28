package org.mamute.migration;


public class NumberExtractor {

	public int from(SchemaMigration clazz) {
		try {
			return Integer.parseInt(clazz.getClass().getSimpleName().substring(1, 4));
		} catch (Exception e) {
			throw new BadFormedMigrationException();
		}
	}

}

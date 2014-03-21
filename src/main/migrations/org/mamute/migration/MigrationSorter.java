package org.mamute.migration;

import java.util.Comparator;

public class MigrationSorter implements Comparator<SchemaMigration> {
	private final NumberExtractor extractNumber;

	public MigrationSorter(NumberExtractor extractNumber) {
		this.extractNumber = extractNumber;
	}

	@Override
	public int compare(SchemaMigration o1, SchemaMigration o2) {
		return extractNumber.from(o1) - extractNumber.from(o2);
	}
}
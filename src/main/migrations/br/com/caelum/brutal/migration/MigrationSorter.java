package br.com.caelum.brutal.migration;

import java.util.Comparator;

public class MigrationSorter implements Comparator<Migration> {
	private final NumberExtractor extractNumber;

	public MigrationSorter(NumberExtractor extractNumber) {
		this.extractNumber = extractNumber;
	}

	@Override
	public int compare(Migration o1, Migration o2) {
		return extractNumber.from(o1) - extractNumber.from(o2);
	}
}
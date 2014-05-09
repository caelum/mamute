package org.mamute.migration;

import java.util.ArrayList;
import java.util.List;

public class RawSQLOperation implements MigrationOperation {
	
	private final String sql;
	
	public RawSQLOperation(String sql) {
		this.sql = sql;
	}

	@Override
	public String execute() {
		return sql;
	}

	public static List<MigrationOperation> forSqls(String... sqls) {
		List<MigrationOperation> operations = new ArrayList<>();
		for (String sql : sqls) {
			operations.add(new RawSQLOperation(sql));
		}
		return operations;
	}
}

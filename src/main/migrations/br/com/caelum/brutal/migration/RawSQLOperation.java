package br.com.caelum.brutal.migration;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class RawSQLOperation implements MigrationOperation {
	
	private final String sql;
	
	public RawSQLOperation(String sql) {
		this.sql = sql;
	}

	@Override
	public void execute(Session session) {
		session.createSQLQuery(sql).executeUpdate();
	}

	public static List<MigrationOperation> forSqls(String... sqls) {
		List<MigrationOperation> operations = new ArrayList<>();
		for (String sql : sqls) {
			operations.add(new RawSQLOperation(sql));
		}
		return operations;
	}
}

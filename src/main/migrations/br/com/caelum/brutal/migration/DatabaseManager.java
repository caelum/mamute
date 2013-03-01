package br.com.caelum.brutal.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.hibernate.Session;

public class DatabaseManager {

	private final Session session;

	public DatabaseManager(Session session) {
		this.session = session;
	}

	public void run(String sql) {
		session.createSQLQuery(sql).executeUpdate();
	}

	public void importAll(String resourceName) {
		run("SET foreign_key_checks = 0;");

		try (InputStream stream = DatabaseManager.class
				.getResourceAsStream(resourceName);
				Scanner sc = new Scanner(stream)) {
			StringBuilder fullQuery = new StringBuilder();
			while (sc.hasNext()) {
				String partial = sc.nextLine();
				boolean isSlashComment = partial.startsWith("/*")
						&& partial.endsWith("*/;");
				boolean isDashComment = partial.startsWith("--");
				boolean isEmpty = partial.trim().isEmpty();
				if (isEmpty || isDashComment || isSlashComment)
					continue;
				fullQuery.append(partial);
				if (partial.endsWith(";")) {
					System.out.println(fullQuery.toString());
					session.createSQLQuery(fullQuery.toString())
							.executeUpdate();
					fullQuery.delete(0, fullQuery.length());
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		run("SET foreign_key_checks = 1;");
	}

}

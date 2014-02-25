package org.mamute.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.enterprise.inject.Vetoed;

import org.apache.log4j.Logger;
import org.hibernate.Session;

@Vetoed
public class DatabaseManager {

	private static final Logger LOG = Logger.getLogger(DatabaseManager.class);
	private Session session;

	public DatabaseManager(Session session) {
		this.session = session;
	}
	
	public void run(String sql) {
		session.createSQLQuery(sql).executeUpdate();
	}

	public void importAll(String resourceName) {
		run("SET foreign_key_checks = 0;");

		try (InputStream stream = DatabaseManager.class.getResourceAsStream(resourceName);
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
					LOG.debug("will execute query:" + fullQuery.toString());
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

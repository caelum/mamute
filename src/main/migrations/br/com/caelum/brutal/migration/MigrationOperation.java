package br.com.caelum.brutal.migration;

import org.hibernate.Session;

public interface MigrationOperation {
	
	void execute(Session session);

}

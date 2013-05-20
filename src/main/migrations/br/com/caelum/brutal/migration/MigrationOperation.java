package br.com.caelum.brutal.migration;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

public interface MigrationOperation {
	
	void execute(Session session, StatelessSession statelessSession);

}

package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleSchemaMigration;
import br.com.caelum.vraptor4.ioc.ApplicationScoped;

@ApplicationScoped
public class M001CreateTableNewsletterSentLog extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "create table NewsletterSentLog ("+
		        "id bigint not null auto_increment,"+
		        "createdAt date,"+
		        "primary key (id)"+
		        ") ENGINE=InnoDB";
	}
	
	@Override
	public String downQuery() {
		return "drop table NewsletterSentLog";
	}

}

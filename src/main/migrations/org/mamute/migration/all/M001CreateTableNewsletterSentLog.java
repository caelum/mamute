package org.mamute.migration.all;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.SimpleSchemaMigration;

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

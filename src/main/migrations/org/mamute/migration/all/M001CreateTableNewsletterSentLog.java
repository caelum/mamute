package org.mamute.migration.all;

import org.mamute.migration.SimpleSchemaMigration;

import javax.enterprise.context.ApplicationScoped;

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

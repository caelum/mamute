package org.mamute.migration.all;


import org.mamute.migration.SimpleSchemaMigration;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class M003AddIndexesToTagPage extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return	"    alter table TagPage \n" + 
				"        add index FK_jcmikqbikgwump3qo3fnqbext (tag_id), \n" + 
				"        add constraint FK_jcmikqbikgwump3qo3fnqbext \n" + 
				"        foreign key (tag_id) \n" + 
				"        references Tag (id)";
	}
	
	@Override
	public String downQuery() {
		return "";
	}

}

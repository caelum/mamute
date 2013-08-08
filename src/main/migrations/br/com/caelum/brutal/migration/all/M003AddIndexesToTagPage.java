package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleSchemaMigration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
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

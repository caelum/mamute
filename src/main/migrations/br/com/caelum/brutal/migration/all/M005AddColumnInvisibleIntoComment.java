package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M005AddColumnInvisibleIntoComment extends Migration{

	@Override
	public String up() {
		return "alter table Comment add column invisible bit(1) default 0";
	}

}

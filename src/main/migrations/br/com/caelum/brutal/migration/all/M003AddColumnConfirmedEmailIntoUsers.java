package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M003AddColumnConfirmedEmailIntoUsers extends Migration{

	@Override
	public String up() {
		return "alter table Users add confirmedEmail bit(1) NOT NULL";
	}
	
	
}

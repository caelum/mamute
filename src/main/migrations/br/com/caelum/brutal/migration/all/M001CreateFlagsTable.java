package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M001CreateFlagsTable extends Migration {

	@Override
	public String up() {
		String createFlag = "CREATE TABLE `Flag` ("
				+ "`id` bigint(20) NOT NULL AUTO_INCREMENT, "
				+ "`reason` longtext, "
				+ "`author_id` bigint(20) DEFAULT NULL, "
				+ "PRIMARY KEY (`id`) "
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1";
		
		return createFlag;
	}

}

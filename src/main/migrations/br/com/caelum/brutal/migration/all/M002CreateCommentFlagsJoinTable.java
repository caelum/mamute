package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M002CreateCommentFlagsJoinTable extends Migration {

	@Override
	public String up() {
		String createJoinTable = "CREATE TABLE `Comment_Flags` ( "
				+ "`Comment_id` bigint(20) NOT NULL, "
				+ "`flags_id` bigint(20) NOT NULL, "
				+ "UNIQUE KEY `flags_id` (`flags_id`), "
				+ "KEY `FKEB33FC671F1F1C7` (`Comment_id`), "
				+ "KEY `FKEB33FC67ED5AAFF2` (`flags_id`), "
				+ "CONSTRAINT `FKEB33FC67ED5AAFF2` FOREIGN KEY (`flags_id`) REFERENCES `Flag` (`id`), "
				+ "CONSTRAINT `FKEB33FC671F1F1C7` FOREIGN KEY (`Comment_id`) REFERENCES `Comment` (`id`)"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		
		return createJoinTable;
	}

}

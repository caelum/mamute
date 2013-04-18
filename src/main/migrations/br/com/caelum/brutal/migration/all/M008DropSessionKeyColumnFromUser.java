package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M008DropSessionKeyColumnFromUser implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String dropColumn = "alter table Users drop column sessionKey";
		return RawSQLOperation.forSqls(dropColumn);
	}

	@Override
	public List<MigrationOperation> down() {
		String recreateColumn = "alter table Users add column sessionKey varchar(255) DEFAULT NULL";
		return RawSQLOperation.forSqls(recreateColumn);
	}

}

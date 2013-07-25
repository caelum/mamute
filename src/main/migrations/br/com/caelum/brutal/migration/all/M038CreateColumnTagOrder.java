package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M038CreateColumnTagOrder implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String alterTable = "alter table QuestionInformation_Tag add column tag_order bigint(20)";
		return forSqls(alterTable);
	}

	@Override
	public List<MigrationOperation> down() {
		return forSqls("alter table QuestionInformation_Tag drop column  tag_order");
	}
}

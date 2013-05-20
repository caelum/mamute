package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M014CreateTableReputationEvent implements Migration {

	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls("create table ReputationEvent (id bigint not null auto_increment, " +
					"karmaReward integer not null, type varchar(255), questionInvolved_id " +
					"bigint, user_id bigint, primary key (id)) ENGINE=InnoDB",
				"alter table ReputationEvent add index FK43AAD981845C688D (user_id), " +
						"add constraint FK43AAD981845C688D foreign key (user_id) references Users (id)",
				"alter table ReputationEvent add index FK43AAD98140A4286 (questionInvolved_id), " +
					"add constraint FK43AAD98140A4286 foreign key (questionInvolved_id) references Question (id)");
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("drop table ReputationEvent");
	}

}

package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M028CreateTableNewsComments implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createNewsComments = "create table News_Comments (News_id bigint not null, comments_id bigint not null, unique (comments_id)) ENGINE=InnoDB";
		String foreignNews = "alter table News_Comments" + 
				"        add index FK8DA695E0FB27AB8D (News_id)," + 
				"        add constraint FK8DA695E0FB27AB8D" + 
				"        foreign key (News_id)" + 
				"        references News (id)";
		String foreignComment = "alter table News_Comments" + 
				"        add index FK8DA695E049C95E12 (comments_id)," + 
				"        add constraint FK8DA695E049C95E12" + 
				"        foreign key (comments_id)" + 
				"        references Comment (id)";
		return RawSQLOperation.forSqls(createNewsComments, foreignNews, foreignComment);
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table News_Comments";
		return RawSQLOperation.forSqls(dropSql);
	}

}

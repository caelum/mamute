package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M031AlterTablesNews implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String foreignNewsInformationNews = "alter table NewsInformation" + 
				"        add index FKA9D05839FB27AB8D (news_id)," + 
				"        add constraint FKA9D05839FB27AB8D" + 
				"        foreign key (news_id)" + 
				"        references News (id)";
		
		String foreignNewsInformationModeratedBy = "alter table NewsInformation" + 
				"        add index FKA9D058397AC17960 (moderatedBy_id)," + 
				"        add constraint FKA9D058397AC17960" + 
				"        foreign key (moderatedBy_id)" + 
				"        references Users (id)";
		
		String foreignNewsInformationAuthor = "alter table NewsInformation " + 
				"        add index FKA9D05839E5155ACD (author_id), " + 
				"        add constraint FKA9D05839E5155ACD " + 
				"        foreign key (author_id) " + 
				"        references Users (id)";
		
		String foreignNewsLastTouchedBy = "alter table News" + 
				"        add index FK24FEF3171098B9 (lastTouchedBy_id)," + 
				"        add constraint FK24FEF3171098B9" + 
				"        foreign key (lastTouchedBy_id)" + 
				"        references Users (id)";
		
		String foreignNewsAuthor = "alter table News" + 
				"        add index FK24FEF3E5155ACD (author_id)," + 
				"        add constraint FK24FEF3E5155ACD" + 
				"        foreign key (author_id)" + 
				"        references Users (id)";
		
		String foreignNewsInformation = "alter table News"  + 
				"        add index FK24FEF3A533D0D4 (information_id)," + 
				"        add constraint FK24FEF3A533D0D4" + 
				"        foreign key (information_id)" + 
				"        references NewsInformation (id)";
		
		
		return RawSQLOperation.forSqls(foreignNewsAuthor, foreignNewsInformation, foreignNewsInformationAuthor,
				foreignNewsInformationModeratedBy, foreignNewsInformationNews, foreignNewsLastTouchedBy);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}

}

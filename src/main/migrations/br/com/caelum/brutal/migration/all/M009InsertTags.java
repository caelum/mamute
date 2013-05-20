package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M009InsertTags implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation insertTags = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				List<String> tags = asList("arredondamento", "linha-de-comando",
						"bashscript", "compressão", "script", "linux");
				for (String tagName : tags) {
					session.save(new Tag(tagName, "", null));
				}
			}
		};
		return asList(insertTags);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}

package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleMigration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M001AddColumnAnswerCountIntoQuestion extends SimpleMigration {

	@Override
	public String rawQuery() {
		return "alter table Question add column answerCount bigint(20)";
	}

}

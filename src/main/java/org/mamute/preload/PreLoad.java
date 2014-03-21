package org.mamute.preload;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.mamute.components.RecentTagsContainer;
import org.mamute.infra.DefaultAdminCreator;
import org.mamute.migration.MigrationRunner;

import br.com.caelum.vraptor.events.VRaptorInitialized;

@ApplicationScoped
public class PreLoad {
	@Inject private MigrationRunner migrations;
	@Inject private RecentTagsContainer tagsContainer;
	@Inject private DefaultAdminCreator adminCreator;
	
	
	public void execute(@Observes VRaptorInitialized initialized) {
		migrations.execute();
		tagsContainer.execute();
		adminCreator.createDefaultAdmin();
	}
	
	@PreDestroy
	public void destroy(){
		tagsContainer.destroy();
	}
	
	
}

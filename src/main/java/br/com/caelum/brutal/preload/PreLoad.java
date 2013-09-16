package br.com.caelum.brutal.preload;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.migration.MigrationRunner;
import br.com.caelum.vraptor4.events.VRaptorInitialized;

@ApplicationScoped
public class PreLoad {
	@Inject private MigrationRunner migrations;
	@Inject private RecentTagsContainer tagsContainer;
	
	
	public void execute(@Observes VRaptorInitialized initialized) {
		migrations.execute();
		tagsContainer.execute();
	}
	
	@PreDestroy
	public void destroy(){
		tagsContainer.destroy();
	}
	
	
}

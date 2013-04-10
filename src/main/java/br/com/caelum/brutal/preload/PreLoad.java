package br.com.caelum.brutal.preload;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.caelum.brutal.components.RecentTagsContainer;
import br.com.caelum.brutal.migration.MigrationRunner;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@ApplicationScoped
@Component
public class PreLoad {
	private final MigrationRunner migrations;
	private final RecentTagsContainer tagsContainer;

	public PreLoad(MigrationRunner migrations, RecentTagsContainer tagsContainer) {
		this.migrations = migrations;
		this.tagsContainer = tagsContainer;
	}
	
	@PostConstruct 
	public void execute() {
		migrations.execute();
		tagsContainer.execute();
	}
	
	@PreDestroy
	public void destroy(){
		tagsContainer.destroy();
	}
	
	
}

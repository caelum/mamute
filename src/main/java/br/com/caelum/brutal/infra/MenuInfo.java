package br.com.caelum.brutal.infra;

import br.com.caelum.brutal.dao.FlaggableDAO;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class MenuInfo {
	
	private final Result result;
	private final LoggedUser loggedUser;
	private final FlaggableDAO flaggables;
	private final InformationDAO informations;

	public MenuInfo(Result result, LoggedUser loggedUser, InformationDAO informations, FlaggableDAO flaggables) {
		this.result = result;
		this.loggedUser = loggedUser;
		this.informations = informations;
		this.flaggables = flaggables;
	}
	
	public void include() {
		result.include("currentUser", loggedUser);
		if (loggedUser.isModerator()) {
			Long pendingCount = informations.pendingCount();
			int flaggedCount = flaggables.flaggedButVisibleCount();
			result.include("pendingForModeratorCount", pendingCount + flaggedCount);
		}
	}

}

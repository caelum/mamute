package br.com.caelum.brutal.infra;

import javax.inject.Inject;

import br.com.caelum.brutal.dao.FlaggableDAO;
import br.com.caelum.brutal.dao.InformationDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor4.Result;

public class MenuInfo {
	
	@Inject private Result result;
	@Inject private LoggedUser loggedUser;
	@Inject private FlaggableDAO flaggables;
	@Inject private InformationDAO informations;

	public void include() {
		result.include("currentUser", loggedUser);
		if (loggedUser.canModerate()) {
			Long pendingCount = informations.pendingCount();
			int flaggedCount = flaggables.flaggedButVisibleCount();
			if (loggedUser.isModerator()) {
				result.include("pendingForModeratorCount", pendingCount + flaggedCount);
			} else {
				result.include("pendingForModeratorCount", pendingCount);
			}
		}
	}

}

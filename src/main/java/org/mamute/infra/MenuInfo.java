package org.mamute.infra;

import javax.inject.Inject;

import org.mamute.dao.FlaggableDAO;
import org.mamute.dao.InformationDAO;
import org.mamute.model.LoggedUser;

import br.com.caelum.vraptor.Result;

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

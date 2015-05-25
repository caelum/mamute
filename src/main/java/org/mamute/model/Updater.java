package org.mamute.model;

import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.model.interfaces.Moderatable;

import javax.inject.Inject;

public class Updater {

	private EnvironmentKarma environmentKarma;

	public Updater(EnvironmentKarma environmentKarma) {
		this.environmentKarma = environmentKarma;
	}

	public UpdateStatus update(Moderatable moderatable, Information information) {
		UpdateStatus status = canUpdate(moderatable, information);
		
		if (status == UpdateStatus.REFUSED && !moderatable.canBeUptadedWith(information))
            return status;
        
        moderatable.enqueueChange(information, status);
		return status;
	}

	private UpdateStatus canUpdate(Moderatable answer, Information newInformation) {
	    User informationAuthor = newInformation.getAuthor();
	    User author = answer.getAuthor();
	    if (author.getId().equals(informationAuthor.getId()) || informationAuthor.canModerate(environmentKarma)) {
	        return UpdateStatus.NO_NEED_TO_APPROVE;
	    }
	    return UpdateStatus.PENDING;
	}
}

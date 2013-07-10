package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public class Updater {

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
	    if (author.getId().equals(informationAuthor.getId()) || informationAuthor.canModerate()) {
	        return UpdateStatus.NO_NEED_TO_APPROVE;
	    }
	    return UpdateStatus.PENDING;
	}
}

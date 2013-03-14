package br.com.caelum.brutal.model.interfaces;

import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;

public abstract class Moderatable implements Identifiable {

    protected abstract Information getInformation();
    protected abstract void updateApproved(Information approved);
    public abstract User getAuthor();
    public abstract String getTypeName();
    
    public final UpdateStatus approve(Information approved) {
        if (!canBeUptadedWith(approved)) {
            throw new IllegalArgumentException("an Answer can only approve an AnswerInformation and a Question can only approve a QuestionInformation");
        }
        updateApproved(approved);
        return UpdateStatus.APPROVED;
    }
    
	private boolean canBeUptadedWith(Information approved) {
		boolean isTheSameImplementation = this.getInformation().getClass().isAssignableFrom(approved.getClass());
        return isTheSameImplementation;
	}


}

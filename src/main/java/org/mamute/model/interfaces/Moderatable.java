package org.mamute.model.interfaces;

import org.mamute.model.Information;
import org.mamute.model.Question;
import org.mamute.model.UpdateStatus;
import org.mamute.model.User;

public abstract class Moderatable implements Identifiable {

    protected abstract Information getInformation();
    protected abstract void updateApproved(Information approved);
    protected abstract void addHistory(Information information);
    public abstract User getAuthor();
    public abstract String getTypeName();
    public abstract boolean hasPendingEdits();
    public abstract Question getQuestion();
    
    public final UpdateStatus approve(Information approved) {
        if (!canBeUptadedWith(approved)) {
            throw new IllegalArgumentException("an Answer can only approve an AnswerInformation and a Question can only approve a QuestionInformation");
        }
        updateApproved(approved);
        return UpdateStatus.APPROVED;
    }
    
	public boolean canBeUptadedWith(Information approved) {
		boolean isTheSameImplementation = this.getInformation().getClass().isAssignableFrom(approved.getClass());
        return isTheSameImplementation;
	}

	public void enqueueChange(Information newInformation, UpdateStatus status) {
		if (status.equals(UpdateStatus.NO_NEED_TO_APPROVE)) {
			updateApproved(newInformation);
		}
		newInformation.setModeratable(this);
        newInformation.setInitStatus(status);
		addHistory(newInformation);
	}
}

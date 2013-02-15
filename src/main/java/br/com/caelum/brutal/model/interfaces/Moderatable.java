package br.com.caelum.brutal.model.interfaces;

import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.UpdateStatus;

public abstract class Moderatable {

    protected abstract Information getInformation();
    
    protected abstract void updateWith(Information approved);
    
    public UpdateStatus approve(Information approved) {
        if (!canBeUptadedWith(approved)) {
            throw new IllegalArgumentException("an answer can only approve an answer information");
        }
        updateWith(approved);
        return UpdateStatus.APPROVED;
    }
    
	private boolean canBeUptadedWith(Information approved) {
		boolean isTheSameImplementation = this.getInformation().getClass().isAssignableFrom(approved.getClass());
        return isTheSameImplementation;
	}

}

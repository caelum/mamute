package br.com.caelum.brutal.model.interfaces;

import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;

public abstract class Moderatable{

    public abstract User getAuthor();

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
		return this.getInformation().getClass().isAssignableFrom(approved.getClass());
	}





}

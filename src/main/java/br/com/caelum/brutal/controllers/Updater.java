package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.HistoryDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class Updater {

    private final User currentUser;

    public Updater(User currentUser) {
        this.currentUser = currentUser;
    }

	public UpdateStatus update(Question original, Question question) {
        UpdateStatus status = currentUser.canUpdate(original);
        if (status == UpdateStatus.REFUSED)
            return status;
        
        original.enqueueChange(question, status);
        return status;
	}

}

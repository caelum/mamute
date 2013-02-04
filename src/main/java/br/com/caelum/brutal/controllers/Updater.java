package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.HistoryDAO;
import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class Updater {

    private final User currentUser;
    private final HistoryDAO edits;

    public Updater(User currentUser, HistoryDAO edits) {
        this.currentUser = currentUser;
        this.edits = edits;
    }

    public UpdateStatus update(Updatable updatable, String field, String value) {
        UpdateStatus status = currentUser.canUpdate(updatable);
        if (status == UpdateStatus.REFUSED)
            return status;
        if (!updatable.update(field, value))
            return UpdateStatus.REFUSED;
        long targetId = (Long) updatable.getId();
        UpdateHistory history = new UpdateHistory(value, updatable.getType(), field, currentUser, status, targetId);
        edits.save(history);
        return status;
    }

}

package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AnswerInformationDAO {

    private final Session session;

    public AnswerInformationDAO(Session session) {
        this.session = session;
    }

}

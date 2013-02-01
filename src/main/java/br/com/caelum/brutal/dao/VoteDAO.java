package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class VoteDAO {
    private final Session session;

    public VoteDAO(Session session) {
        this.session = session;
    }

    public void save(Vote vote) {
        session.save(vote);
    }
}

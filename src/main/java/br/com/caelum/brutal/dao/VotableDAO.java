package br.com.caelum.brutal.dao;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.VoteType;

public interface VotableDAO {

    boolean alreadyVoted(Long id, User author, VoteType type);

}

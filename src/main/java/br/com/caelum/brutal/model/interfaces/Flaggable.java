package br.com.caelum.brutal.model.interfaces;

import br.com.caelum.brutal.model.Flag;
import br.com.caelum.brutal.model.User;

public interface Flaggable {

	public void add(Flag flag);
	public boolean alreadyFlaggedBy(User user);

}

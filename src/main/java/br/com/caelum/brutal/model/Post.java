package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Commentable;
import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.brutal.model.interfaces.Touchable;
import br.com.caelum.brutal.model.interfaces.Votable;

public interface Post extends Votable, Commentable, Touchable, Flaggable {

}

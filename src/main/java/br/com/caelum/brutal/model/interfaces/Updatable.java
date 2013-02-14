package br.com.caelum.brutal.model.interfaces;

import br.com.caelum.brutal.model.User;

public interface Updatable extends Identifiable {

    User getAuthor();
    
    String getTypeName();

}

package br.com.caelum.brutal.model;

import java.io.Serializable;


public interface Updatable {

    boolean update(String field, String value);

    User getAuthor();

    String getTypeName();

    Serializable getId();

    /**
     * Returns its own type. We need it because Hibernate creates proxies around
     * classes so we dont know what is the actual type unless we fuzzle into
     * Hibernates API or ignore specific proxy types.
     * 
     * @return
     */
    Class<?> getType();

}

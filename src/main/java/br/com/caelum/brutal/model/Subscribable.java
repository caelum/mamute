package br.com.caelum.brutal.model;

import org.joda.time.DateTime;

public interface Subscribable {
    
    public String getTrimmedContent();
    public DateTime getCreatedAt();
    public String getTypeNameKey();
    
}

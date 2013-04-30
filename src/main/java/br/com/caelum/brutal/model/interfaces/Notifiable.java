package br.com.caelum.brutal.model.interfaces;

import org.joda.time.DateTime;

public interface Notifiable {
    public String getTrimmedContent();
    public DateTime getCreatedAt();
    public String getTypeNameKey();
    public String getEmailTemplate();
}

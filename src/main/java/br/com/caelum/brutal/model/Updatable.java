package br.com.caelum.brutal.model;

public interface Updatable extends Identifiable {

    User getAuthor();

    String getTypeName();

    UpdateStatus approve(Information approved);

    void moderateCurrentInformation(User user, UpdateStatus edited);

}

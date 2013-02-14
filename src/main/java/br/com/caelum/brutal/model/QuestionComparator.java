package br.com.caelum.brutal.model;

import java.util.Comparator;

import br.com.caelum.brutal.model.interfaces.Updatable;

public class QuestionComparator implements Comparator<Updatable> {

    @Override
    public int compare(Updatable question1, Updatable question2) {
        if (question1.getId().equals(question2.getId()))
            return 0;
        return (Long) question1.getId() - (Long) question2.getId() < 0l ? -1 : 1;
    }

}

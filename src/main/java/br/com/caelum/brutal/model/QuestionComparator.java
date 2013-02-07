package br.com.caelum.brutal.model;

import java.util.Comparator;

public class QuestionComparator implements Comparator<Question> {

    @Override
    public int compare(Question question1, Question question2) {
        if (question1.getId().equals(question2.getId()))
            return 0;
        return question1.getId() - question2.getId() < 0l ? -1 : 1;
    }

}

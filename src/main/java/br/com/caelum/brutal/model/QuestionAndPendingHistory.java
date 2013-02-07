package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class QuestionAndPendingHistory {

    private TreeMap<Question, List<QuestionInformation>> informationsByQuestion;

    public QuestionAndPendingHistory(List<Object[]> questionAndInformations) {
        QuestionComparator comparator = new QuestionComparator();
        informationsByQuestion = new TreeMap<>(comparator);
        for (Object[] questionAndInformation : questionAndInformations) {
            Question question = (Question) questionAndInformation[0]; 
            QuestionInformation questionInformation = (QuestionInformation) questionAndInformation[1];
            
            List<QuestionInformation> informations = informationsByQuestion.get(question);
            if (informations == null) {
                informations = new ArrayList<>();
            }
            informations.add(questionInformation);
            informationsByQuestion.put(question, informations);
        }
    }
    
    public List<Question> questions() {
        return new ArrayList<Question>(informationsByQuestion.keySet());
    }

    public List<QuestionInformation> pendingInfoFor(Question question) {
        return informationsByQuestion.get(question);
    }

}

package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class UpdatablesAndPendingHistory {

    private TreeMap<Question, List<QuestionInformation>> informationsByUpdatable;

    public UpdatablesAndPendingHistory(List<Object[]> questionAndInformations) {
        QuestionComparator comparator = new QuestionComparator();
        informationsByUpdatable = new TreeMap<>(comparator);
        for (Object[] questionAndInformation : questionAndInformations) {
            Question question = (Question) questionAndInformation[0]; 
            QuestionInformation questionInformation = (QuestionInformation) questionAndInformation[1];
            
            List<QuestionInformation> informations = informationsByUpdatable.get(question);
            if (informations == null) {
                informations = new ArrayList<>();
            }
            informations.add(questionInformation);
            informationsByUpdatable.put(question, informations);
        }
    }
    
    public List<Question> questions() {
        return new ArrayList<Question>(informationsByUpdatable.keySet());
    }
    
    public Set<Entry<Question, List<QuestionInformation>>> questionsEntrySet() {
        return informationsByUpdatable.entrySet();
    }

    public List<QuestionInformation> pendingInfoFor(Question question) {
        return informationsByUpdatable.get(question);
    }

}

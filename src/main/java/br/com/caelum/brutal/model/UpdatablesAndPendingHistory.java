package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class UpdatablesAndPendingHistory {

    private TreeMap<Updatable, List<UpdatableInformation>> informationsByUpdatable;

    public UpdatablesAndPendingHistory(List<Object[]> questionAndInformations) {
        QuestionComparator comparator = new QuestionComparator();
        informationsByUpdatable = new TreeMap<>(comparator);
        for (Object[] questionAndInformation : questionAndInformations) {
            Updatable question = (Updatable) questionAndInformation[0]; 
            UpdatableInformation questionInformation = (UpdatableInformation) questionAndInformation[1];
            
            List<UpdatableInformation> informations = informationsByUpdatable.get(question);
            if (informations == null) {
                informations = new ArrayList<>();
            }
            informations.add(questionInformation);
            informationsByUpdatable.put(question, informations);
        }
    }
    
    public List<Updatable> questions() {
        return new ArrayList<Updatable>(informationsByUpdatable.keySet());
    }
    
    public Set<Entry<Updatable, List<UpdatableInformation>>> getEntrySet() {
        return informationsByUpdatable.entrySet();
    }

    public List<UpdatableInformation> pendingInfoFor(Updatable question) {
        return informationsByUpdatable.get(question);
    }

}

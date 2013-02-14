package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import br.com.caelum.brutal.model.interfaces.Updatable;

public class UpdatablesAndPendingHistory {

    private TreeMap<Updatable, List<Information>> informationsByUpdatable;

    public UpdatablesAndPendingHistory(List<Object[]> questionAndInformations) {
        QuestionComparator comparator = new QuestionComparator();
        informationsByUpdatable = new TreeMap<>(comparator);
        for (Object[] questionAndInformation : questionAndInformations) {
            Updatable question = (Updatable) questionAndInformation[0]; 
            Information questionInformation = (Information) questionAndInformation[1];
            
            List<Information> informations = informationsByUpdatable.get(question);
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
    
    public Set<Entry<Updatable, List<Information>>> getEntrySet() {
        return informationsByUpdatable.entrySet();
    }

    public List<Information> pendingInfoFor(Updatable question) {
        return informationsByUpdatable.get(question);
    }

}

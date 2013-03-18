package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public class UpdatablesAndPendingHistory {

    private TreeMap<Moderatable, List<Information>> informationsByUpdatable;

    public UpdatablesAndPendingHistory(List<Object[]> questionAndInformations) {
        ModeratableComparator comparator = new ModeratableComparator();
        informationsByUpdatable = new TreeMap<>(comparator);
        for (Object[] moderatableAndInformation : questionAndInformations) {
            Moderatable moderatable = (Moderatable) moderatableAndInformation[0]; 
            Information questionInformation = (Information) moderatableAndInformation[1];
            
            List<Information> informations = informationsByUpdatable.get(moderatable);
            if (informations == null) {
                informations = new ArrayList<>();
            }
            informations.add(questionInformation);
            informationsByUpdatable.put(moderatable, informations);
        }
    }
    
    public List<Moderatable> moderatables() {
        return new ArrayList<Moderatable>(informationsByUpdatable.keySet());
    }
    
    public Set<Entry<Moderatable, List<Information>>> getEntrySet() {
        return informationsByUpdatable.entrySet();
    }

    public List<Information> pendingInfoFor(Moderatable moderatable) {
        return informationsByUpdatable.get(moderatable);
    }

}

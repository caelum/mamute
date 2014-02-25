package org.mamute.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.mamute.model.interfaces.Moderatable;

public class ModeratableAndPendingHistory {

    private TreeMap<Moderatable, List<Information>> informationsByModeratable;

    public ModeratableAndPendingHistory(List<Object[]> questionAndInformations) {
        ModeratableComparator comparator = new ModeratableComparator();
        informationsByModeratable = new TreeMap<>(comparator);
        for (Object[] moderatableAndInformation : questionAndInformations) {
            Moderatable moderatable = (Moderatable) moderatableAndInformation[0]; 
            Information questionInformation = (Information) moderatableAndInformation[1];
            
            List<Information> informations = informationsByModeratable.get(moderatable);
            if (informations == null) {
                informations = new ArrayList<>();
            }
            informations.add(questionInformation);
            informationsByModeratable.put(moderatable, informations);
        }
    }
    
    public List<Moderatable> moderatables() {
        return new ArrayList<Moderatable>(informationsByModeratable.keySet());
    }
    
    public Set<Entry<Moderatable, List<Information>>> getEntrySet() {
        return informationsByModeratable.entrySet();
    }

    public List<Information> pendingInfoFor(Moderatable moderatable) {
        return informationsByModeratable.get(moderatable);
    }

}

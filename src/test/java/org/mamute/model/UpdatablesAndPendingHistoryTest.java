package org.mamute.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.Information;
import org.mamute.model.ModeratableAndPendingHistory;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.QuestionInformationBuilder;
import org.mamute.model.User;

public class UpdatablesAndPendingHistoryTest extends TestCase {

    @Test
    public void should_build_group_informations_by_updatable() {
        List<Object[]> updatableAndInformations = new ArrayList<>();
        User author =  user("name", "emaiil");
        
        QuestionInformationBuilder builder = new QuestionInformationBuilder().with(author);
        QuestionInformation info1 = builder.withTitle("title1").build();
        QuestionInformation info2 = builder.withTitle("title2").build();
        Question question1 = new Question(info1, author);
        question1.setId(1l);
        
        QuestionInformation info3 = builder.withTitle("title3").build();
        QuestionInformation info4 = builder.withTitle("title4").build();
        Question question2 = new Question(info3, author);
        question2.setId(2l);
        
        updatableAndInformations.add(new Object[]{question1, info1});
        updatableAndInformations.add(new Object[]{question1, info2});
        updatableAndInformations.add(new Object[]{question2, info3});
        updatableAndInformations.add(new Object[]{question2, info4});
        
        ModeratableAndPendingHistory updatablesAndPendingHistory = new ModeratableAndPendingHistory(updatableAndInformations);
        
        List<Information> pendingInfoForQ1 = updatablesAndPendingHistory.pendingInfoFor(question1);
        List<Information> pendingInfoForQ2 = updatablesAndPendingHistory.pendingInfoFor(question2);
        
        assertEquals(2, pendingInfoForQ1.size());
        assertEquals("title1", ((QuestionInformation) pendingInfoForQ1.get(0)).getTitle());
        assertEquals("title2", ((QuestionInformation) pendingInfoForQ1.get(1)).getTitle());
        
        assertEquals(2, pendingInfoForQ2.size());
        assertEquals("title3", ((QuestionInformation) pendingInfoForQ2.get(0)).getTitle());
        assertEquals("title4", ((QuestionInformation) pendingInfoForQ2.get(1)).getTitle());
    }

}

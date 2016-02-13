package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.MarkedText.notMarked;
import static org.mamute.model.UpdateStatus.PENDING;
import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;

public class AnswerInformationTest {

	private Answer ruby;

	@Before
	public void setup(){
		AnswerInformation rubyInformation = newVersion(100);
		QuestionBuilder builder = new QuestionBuilder();
		ruby = new Answer(rubyInformation, builder.build() , null);
	}
	
	@Test
	public void should_verify_if_is_before_current_information() {
	
		AnswerInformation version = newVersion(10);
		ruby.enqueueChange(version, PENDING);
		AnswerInformation infoByModerator = newVersion(5);
		ruby.approve(infoByModerator);

		assertTrue(version.isBeforeCurrent());
	}

	@Test
	public void should_verify_if_is_not_before_current_information() {
		AnswerInformation infoByModerator = newVersion(10);
		ruby.approve(infoByModerator);
		AnswerInformation version = newVersion(5);
		ruby.enqueueChange(version, PENDING);

		assertFalse(version.isBeforeCurrent());
	}
	
	private AnswerInformation newVersion(int minus) {
		return TimeMachine.goTo(new DateTime().minusSeconds(minus)).andExecute(new Block<AnswerInformation>() {
			@Override
			public AnswerInformation run() {
				return new AnswerInformation(notMarked("do this and that with ruby like that: lol"), null, "");
			}
		});
	}
}

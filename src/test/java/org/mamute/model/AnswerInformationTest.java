package org.mamute.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mamute.model.UpdateStatus.PENDING;

import org.junit.Before;
import org.junit.Test;
import org.mamute.builder.QuestionBuilder;
import org.mamute.model.Answer;
import org.mamute.model.AnswerInformation;

public class AnswerInformationTest {

	private Answer ruby;

	@Before
	public void setup(){
		AnswerInformation rubyInformation = newVersion();
		QuestionBuilder builder = new QuestionBuilder();
		ruby = new Answer(rubyInformation, builder.build() , null);
	}
	
	@Test
	public void should_verify_if_is_before_current_information() {
	
		AnswerInformation version = newVersion();
		ruby.enqueueChange(version, PENDING);
		AnswerInformation infoByModerator = newVersion();
		ruby.approve(infoByModerator);
		
		assertTrue(version.isBeforeCurrent());
	}

	@Test
	public void should_verify_if_is_not_before_current_information() {
		AnswerInformation infoByModerator = newVersion();
		ruby.approve(infoByModerator);
		AnswerInformation version = newVersion();
		ruby.enqueueChange(version, PENDING);
		
		
		assertFalse(version.isBeforeCurrent());
	}
	
	private AnswerInformation newVersion() {
		return new AnswerInformation("do this and that with ruby like that: lol", null, "");
	}
}

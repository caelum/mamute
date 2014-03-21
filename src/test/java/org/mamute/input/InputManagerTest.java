package org.mamute.input;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.input.InputManager;
import org.mamute.model.User;

import br.com.caelum.timemachine.Block;
import br.com.caelum.timemachine.TimeMachine;

public class InputManagerTest extends TestCase {
	final InputManager input = new InputManager();
	final User leo = user("Leonardo", "leo@leo", 1l);

	@Test
	public void should_allow_if_after_15_secconds() {
		input.ping(leo);
		TimeMachine.goTo(new DateTime().plusSeconds(16)).andExecute(new Block<Void>() {
			@Override
			public Void run() {
				assertTrue(input.can(leo));
				return null;
			}
		});
	}
	
	@Test
	public void should_not_allow_if_before_15_secconds() {
		input.ping(leo);
		assertFalse(input.can(leo));
	}
	
	@Test
	public void should_get_the_remaining_time() {
		input.ping(leo);
		TimeMachine.goTo(new DateTime().plusSeconds(5)).andExecute(new Block<Void>() {
			@Override
			public Void run() {
				assertEquals(10 ,input.getRemainingTime(leo));
				return null;
			}
		});		
	}

}

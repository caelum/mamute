package org.mamute.model.flag;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.mamute.dao.TestCase;
import org.mamute.model.flag.FlagAction;
import org.mamute.model.flag.FlagTrigger;
import org.mamute.model.interfaces.Flaggable;
import org.mockito.Mockito;

public class FlagTriggerTest extends TestCase {

	@Test
	public void should_trigger_correct_actions() {
		FlagAction neverExecuteAction = mock(FlagAction.class);
		FlagAction alwaysExecuteAction = mock(FlagAction.class);
		Flaggable flaggable = mock(Flaggable.class);
		when(neverExecuteAction.shouldHandle(Mockito.any(Flaggable.class))).thenReturn(false);
		when(alwaysExecuteAction.shouldHandle(Mockito.any(Flaggable.class))).thenReturn(true);
		
		FlagTrigger flagTrigger = new FlagTrigger(Arrays.asList(neverExecuteAction, alwaysExecuteAction));
		
		flagTrigger.fire(flaggable);
		
		verify(neverExecuteAction, never()).fire(flaggable);
		verify(alwaysExecuteAction, times(1)).fire(flaggable);
	}

}

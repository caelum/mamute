package br.com.caelum.brutal.input;

import static org.joda.time.Seconds.secondsBetween;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor4.ioc.ApplicationScoped;

@ApplicationScoped
public class InputManager {

	private static final int SPAM_TIME = 15;
	private Map<Long, DateTime> lastInput = new HashMap<>();
	
	public boolean can(User user) {
		DateTime when = lastInput.get(user.getId());
		if(when == null) return true;
		return when.isBefore(new DateTime().minusSeconds(SPAM_TIME));
	}

	public void ping(User user) {
		lastInput.put(user.getId(), new DateTime());
	}

	public int getRemainingTime(User user) {
		return SPAM_TIME - secondsBetween(lastInput.get(user.getId()), new DateTime()).getSeconds();
	}

}

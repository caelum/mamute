package br.com.caelum.brutal.infra;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Component
public class AfterSuccessfulTransaction {

	private List<Runnable> actions = new ArrayList<>();
	
	public void execute(Runnable action) {
		actions.add(action);
	}
	
	public void run() {
		for (Runnable action : actions) {
			action.run();
		}
	}

}

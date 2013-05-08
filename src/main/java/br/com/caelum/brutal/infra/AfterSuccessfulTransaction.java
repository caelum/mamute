package br.com.caelum.brutal.infra;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.RequestScoped;

@RequestScoped
@Component
public class AfterSuccessfulTransaction {

	private List<PostTransactionAction> actions = new ArrayList<>();
	
	public void execute(PostTransactionAction action) {
		actions.add(action);
	}
	
	public void run() {
		for (PostTransactionAction action : actions) {
			action.execute();
		}
	}

}

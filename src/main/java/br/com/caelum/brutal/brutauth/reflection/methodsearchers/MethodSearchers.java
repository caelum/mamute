package br.com.caelum.brutal.brutauth.reflection.methodsearchers;

import java.util.List;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.BrutauthMethod;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class MethodSearchers {
	private final List<MethodSearcher> searchers;

	public MethodSearchers(List<MethodSearcher> searchers) {
		this.searchers = searchers;
	}
	
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object...withArgs){
		for (MethodSearcher searcher : searchers) {
			BrutauthMethod brutauthMethod = searcher.search(ruleToSearch, withArgs);
			if(brutauthMethod != null) return brutauthMethod;
		}
		throw new IllegalStateException("Não achei nenhum metodo para invocar na rule "+ruleToSearch.getClass().getSimpleName());
	}
}

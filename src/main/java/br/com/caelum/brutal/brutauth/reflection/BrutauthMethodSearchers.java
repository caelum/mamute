package br.com.caelum.brutal.brutauth.reflection;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BrutauthMethodSearchers {
	private final List<BrutauthMethodSearcher> searchers;

	public BrutauthMethodSearchers(ArrayList<BrutauthMethodSearcher> searchers) {
		this.searchers = searchers;
	}
	
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object...withArgs){
		for (BrutauthMethodSearcher searcher : searchers) {
			BrutauthMethod brutauthMethod = searcher.search(ruleToSearch, withArgs);
			if(brutauthMethod != null) return brutauthMethod;
		}
		throw new IllegalStateException("Não achei nenhum metodo com nome isAllowed que receba os metodos de sua action na rule "+ruleToSearch.getClass().getSimpleName());
	}
}

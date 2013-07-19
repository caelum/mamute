package br.com.caelum.brutal.brutauth.reflection;

import br.com.caelum.brutal.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutal.brutauth.reflection.methodsearchers.BrutauthMethodSearchers;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultMethodInvoker {

		private final BrutauthMethodSearchers searcher;

		public DefaultMethodInvoker(BrutauthMethodSearchers searcher) {
			this.searcher = searcher;
		}
	
		public boolean invoke(CustomBrutauthRule toInvoke, Object[] args) {
			return searcher.search(toInvoke, args).invoke();
		}
	

}

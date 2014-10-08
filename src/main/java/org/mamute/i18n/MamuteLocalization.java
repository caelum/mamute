package org.mamute.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;

import br.com.caelum.vraptor.core.JstlLocalization;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class MamuteLocalization extends JstlLocalization{

	private final HttpServletRequest request;

	/**
	 * @deprecated CDI eyes only
	 */
	protected MamuteLocalization() {
		this(null);
	}
	
	@Inject
	public MamuteLocalization(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	
	
	@Override @Produces
	public ResourceBundle getBundle(Locale locale) {
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, null);
		ResourceBundle customBundle = super.getBundle(locale);
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, "mamute-messages");
		ResourceBundle mamuteBundle = super.getBundle(locale);
		
		return new MamuteResourceBundle(customBundle, mamuteBundle);
	}
	
	@Override @Produces
	public Locale getLocale() {
		return super.getLocale();
	}
	
}

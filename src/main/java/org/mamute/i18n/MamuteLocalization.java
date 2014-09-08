package org.mamute.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;

import br.com.caelum.vraptor.core.JstlLocalization;

@Specializes
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
	public ResourceBundle getBundle() {
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, null);
		ResourceBundle customBundle = super.getBundle();
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, "mamute-messages");
		ResourceBundle mamuteBundle = super.getBundle();
		
		return new MamuteResourceBundle(customBundle, mamuteBundle);
	}
	
	@Override @Produces
	public Locale getLocale() {
		return super.getLocale();
	}
	
}

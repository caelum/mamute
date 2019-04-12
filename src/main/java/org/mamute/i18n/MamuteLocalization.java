package org.mamute.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import br.com.caelum.vraptor.core.JstlLocalization;
import br.com.caelum.vraptor.util.EmptyBundle;
import br.com.caelum.vraptor.util.SafeResourceBundle;
import com.google.common.base.Objects;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class MamuteLocalization extends JstlLocalization {

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
		ResourceBundle customBundle = getUTF8Bundle(locale);
		Config.set(request, Config.FMT_LOCALIZATION_CONTEXT, "mamute-messages");
		ResourceBundle mamuteBundle = getUTF8Bundle(locale);

		return new MamuteResourceBundle(customBundle, mamuteBundle);
	}

	public ResourceBundle getUTF8Bundle(Locale locale) {
		Object bundle = this.findByKey("javax.servlet.jsp.jstl.fmt.localizationContext");
		ResourceBundle unsafe = this.extractUnsafeBundle(bundle, locale);
		return new SafeResourceBundle(unsafe);
	}


	private ResourceBundle extractUnsafeBundle(Object bundle, Locale locale) {
		if (!(bundle instanceof String) && bundle != null) {
			if (bundle instanceof LocalizationContext) {
				return ((LocalizationContext) bundle).getResourceBundle();
			} else {
				return new EmptyBundle();
			}
		} else {
			String baseName = Objects.firstNonNull((String) bundle, "messages");
			try {
				if (locale.getLanguage().equals("fa"))
					return ResourceBundle.getBundle(baseName, locale, new UTF8Control());
				else
					return ResourceBundle.getBundle(baseName, locale);
			} catch (MissingResourceException var5) {
				return new EmptyBundle();
			}
		}
	}

	private Object findByKey(String key) {
		Object value = Config.get(this.request, key);
		if (value != null) {
			return value;
		} else {
			value = Config.get(this.request.getSession(), key);
			if (value != null) {
				return value;
			} else {
				value = Config.get(this.request.getServletContext(), key);
				return value != null ? value : this.request.getServletContext().getInitParameter(key);
			}
		}
	}

	@Override @Produces
	public Locale getLocale() {
		return super.getLocale();
	}

}

package br.com.caelum.brutal.components;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.ServletContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.core.SafeResourceBundle;
import br.com.caelum.vraptor.util.EmptyBundle;

import com.google.common.base.Strings;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
@RequestScoped
public class OutOfSessionLocalization implements Localization {

    private static final Logger logger = LoggerFactory.getLogger(OutOfSessionLocalization.class);

    private static final String DEFAULT_BUNDLE_NAME = "messages";

    private RequestInfo request;
    private ResourceBundle bundle;

    //CDI eyes only
	@Deprecated
	public OutOfSessionLocalization() {
	}
    
    @Inject
    public OutOfSessionLocalization(RequestInfo request) {
        this.request = request;
    }

	public ResourceBundle getBundle() {
        if (bundle == null) {
            initializeBundle();
        }

        return bundle;
    }

    /**
     * Find the bundle. If the bundle is not found, return an empty for safety operations (avoid
     * {@link MissingResourceException}.
     */
    private void initializeBundle() {
        Object bundle = findByKey(Config.FMT_LOCALIZATION_CONTEXT);
        ResourceBundle unsafe = extractUnsafeBundle(bundle);

        this.bundle = new SafeResourceBundle(unsafe);
    }

	private ResourceBundle extractUnsafeBundle(Object bundle) {
        if (bundle instanceof String || bundle == null) {
            String baseName = (bundle == null) ? DEFAULT_BUNDLE_NAME : bundle.toString();

            try {
                return ResourceBundle.getBundle(baseName, getLocale());
            } catch (MissingResourceException e) {
                logger.debug("couldn't find message bundle, creating an empty one");
                return new EmptyBundle();
            }

        }
        if (bundle instanceof LocalizationContext) {
            return ((LocalizationContext) bundle).getResourceBundle();
        }
        logger.warn("Can't handle bundle {}. Please report this bug. Using an empty bundle", bundle);
        return new EmptyBundle();
	}

	public Locale getLocale() {
        return localeFor(Config.FMT_LOCALE);
    }

	public Locale getFallbackLocale() {
        return localeFor(Config.FMT_FALLBACK_LOCALE);
    }

    private Locale localeFor(String key) {
        Object localeValue = findByKey(key);

        if (localeValue instanceof String) {
            return findLocalefromString((String) localeValue);
        } else if (localeValue instanceof Locale) {
            return (Locale) localeValue;
        }

        return request.getRequest().getLocale();
    }

    /**
     * Looks up a configuration variable in the request, session and application scopes. If none is found, return by
     * {@link ServletContext#getInitParameter(String)} method.
     *
     * @param key
     * @return
     */
    private Object findByKey(String key) {
        Object value = Config.get(request.getRequest(), key);
        if (value != null) {
            return value;
        }

        value = Config.get(request.getServletContext(), key);
        if (value != null) {
            return value;
        }

        return request.getServletContext().getInitParameter(key);
    }

	public String getMessage(String key, Object... parameters) {
        try {
            String content = getBundle().getString(key);
            return MessageFormat.format(content, parameters);
        } catch (MissingResourceException e) {
            return "???" + key + "???";
        }
    }

    /**
     * Converts a locale string to {@link Locale}. If the input string is null or empty, return an empty {@link Locale}.
     *
     * @param str
     * @return
     */
    private Locale findLocalefromString(String str) {
        if (!Strings.isNullOrEmpty(str)) {
            String[] arr = str.split("_");
            if (arr.length == 1) {
                return new Locale(arr[0]);
            } else if (arr.length == 2) {
                return new Locale(arr[0], arr[1]);

            } else {
                return new Locale(arr[0], arr[1], arr[2]);
            }
        }

        return null;
    }
}

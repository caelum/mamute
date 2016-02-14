package org.mamute.template;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

/**
 * @author felipeweb
 *
 */
@Named("templateFinder")
public class CustomTemplatesFinder {
	protected static final String CUSTOM_TEMPLATE_URI = "/WEB-INF/jsp/theme/custom/";
	protected static final String EXTENSION = ".jspf";
	@Inject
	private ServletContext application;

	public boolean exists(String fileName) {
		File customTemplate = new File(application.getRealPath(CUSTOM_TEMPLATE_URI
				+ fileName + EXTENSION));
		return customTemplate.exists();
	}
}

package org.mamute.sanitizer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.owasp.html.ElementPolicy;

public class HtmlLinkTargetElementPolicy implements ElementPolicy {

	@Override
	public String apply(final String elementName, final List<String> attrs) {
		// Disallow the whole element if it does not have any allowed attributes.
		if (CollectionUtils.isEmpty(attrs)) {
			return null;
		}

		attrs.add("target");
		attrs.add("_blank");

		return elementName;
	}

}

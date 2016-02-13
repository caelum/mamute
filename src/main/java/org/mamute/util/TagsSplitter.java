package org.mamute.util;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Property;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class TagsSplitter {

	private final String regex;

	/**
	 * @deprecated CDI eyes only
	 */
	public TagsSplitter() {
		this("");
	}
	
	@Inject
	public TagsSplitter(@Property("tags.splitter.regex") String regex) {
		this.regex = regex;
	}
	
	public List<String> splitTags(String tagNames) {
		List<String> tags = tagNames == null ? new ArrayList<String>() : asList(tagNames.split(regex));
		Collection<String> trimmed = transform(tags, trim());
		Collection<String> notEmpty = filter(trimmed, filterEmpty());
		return new ArrayList<>(notEmpty);
	}

	private Predicate<String> filterEmpty() {
		return new Predicate<String>() {
			@Override
			public boolean apply(String input) {
				return !input.isEmpty();
			}
		};
	}

	private Function<String, String> trim() {
		return new Function<String, String>() {
			@Override
			public String apply(String input) {
				return input.trim();
			}
		};
	}
	
}

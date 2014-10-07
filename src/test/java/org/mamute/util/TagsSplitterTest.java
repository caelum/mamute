package org.mamute.util;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

public class TagsSplitterTest {

	@Test
	public void should_split_tags_based_on_regex_for_comma() {
		TagsSplitter tagsSplitter = new TagsSplitter("\\,");
		List<String> splitTags = tagsSplitter.splitTags("java, ruby");
		assertThat(splitTags, hasItems("java", "ruby"));
	}
	
	@Test
	public void should_split_tags_based_on_regex_for_space_character() {
		TagsSplitter tagsSplitter = new TagsSplitter("\\s+");
		List<String> splitTags = tagsSplitter.splitTags("java ruby");
		assertThat(splitTags, hasItems("java", "ruby"));
	}
	
	@Test
	public void should_split_tags_based_on_regex_for_both_comma_and_space_character() {
		TagsSplitter tagsSplitter = new TagsSplitter("[\\s+|\\,]");
		List<String> splitTags = tagsSplitter.splitTags("java ruby,c");
		assertThat(splitTags, hasItems("java", "ruby", "c"));
	}

}

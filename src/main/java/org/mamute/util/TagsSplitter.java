package org.mamute.util;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class TagsSplitter {
	
	public static List<String> splitTags(String tagNames) {
		return tagNames == null ? new ArrayList<String>() : asList(tagNames.split("\\s+"));
	}
	
}

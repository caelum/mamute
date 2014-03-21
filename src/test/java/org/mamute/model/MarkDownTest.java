package org.mamute.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mamute.model.MarkDown;

public class MarkDownTest {
	
	@Test
	public void should_parse_markdown_as_markdown() {
		String result = MarkDown.parse("oi *guilherme*, tudo bem?");
		assertEquals("<p>oi <em>guilherme</em>, tudo bem?</p>\n", result);
	}
	
	@Test
	public void should_suport_three_backticks() {
		String result = MarkDown.parse("```\npublic void main(String[] args)\n```");
		assertEquals("<pre class=\"prettyprint\"><code>public void main(String[] args)</code></pre>\n", result);
	}

}

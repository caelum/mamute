package br.com.caelum.brutal.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MarkDownTest {
	
	@Test
	public void should_parse_markdown_as_markdown() {
		String result = MarkDown.parse("oi *guilherme*, tudo bem?");
		assertEquals("<p>oi <em>guilherme</em>, tudo bem?</p>", result);
	}

}

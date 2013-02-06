package br.com.caelum.brutal.model;

public class TagUsage {
	private Tag tag;
	private Long usage;

	public TagUsage(Tag tag, Long usage) {
		this.tag = tag;
		this.usage = usage;
	}

	public Tag getTag() {
		return tag;
	}

	public Long getUsage() {
		return usage;
	}

}

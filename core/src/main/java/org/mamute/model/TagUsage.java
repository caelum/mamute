package org.mamute.model;

public class TagUsage {
	private final Tag tag;
	private final Long usage;

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

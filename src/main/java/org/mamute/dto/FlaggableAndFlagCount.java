package org.mamute.dto;

import org.mamute.model.interfaces.Flaggable;

public class FlaggableAndFlagCount {
	
	private final Flaggable flaggable;
	private final Long flagCount;

	public FlaggableAndFlagCount(Flaggable comment, Long FlagCount) {
		this.flaggable = comment;
		flagCount = FlagCount;
	}

	public Flaggable getFlaggable() {
		return flaggable;
	}

	public long getFlagCount() {
		return flagCount;
	}

}

package br.com.caelum.brutal.dto;

import br.com.caelum.brutal.model.interfaces.Flaggable;

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

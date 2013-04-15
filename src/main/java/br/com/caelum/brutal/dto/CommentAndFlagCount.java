package br.com.caelum.brutal.dto;

import br.com.caelum.brutal.model.Comment;

public class CommentAndFlagCount {
	
	private final Comment comment;
	private final Long flagCount;

	public CommentAndFlagCount(Comment comment, Long FlagCount) {
		this.comment = comment;
		flagCount = FlagCount;
	}

	public Comment getComment() {
		return comment;
	}

	public long getFlagCount() {
		return flagCount;
	}

}

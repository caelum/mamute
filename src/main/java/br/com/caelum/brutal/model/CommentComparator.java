package br.com.caelum.brutal.model;

import java.util.Comparator;

public class CommentComparator implements Comparator<Comment>{

	@Override
	public int compare(Comment o1, Comment o2) {
		return o1.getCreatedAt().compareTo(o2.getCreatedAt());
	}
	
}

package org.mamute.dto;

import org.mamute.model.User;

public class UserSummaryForTag {
	private final Long karmaEarned;
	private final Long count;
	private final User user;

	public UserSummaryForTag(Long karmaEarned, Long count, User user) {
		this.karmaEarned = karmaEarned;
		this.count = count;
		this.user = user;
	}
	
	public Long getCount() {
		return count;
	}
	
	public Long getKarmaReward() {
		return karmaEarned;
	}
	
	public User getUser() {
		return user;
	}
}

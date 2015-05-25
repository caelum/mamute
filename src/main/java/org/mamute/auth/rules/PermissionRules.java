package org.mamute.auth.rules;

public enum PermissionRules {
	/**
	 * to vote up anything
	 */
	VOTE_UP("vote_up"),

	/**
	 * to vote down anything
	 */
	VOTE_DOWN("vote_down"),
	
	/**
	 * to answer own question
	 */
	ANSWER_OWN_QUESTION("answer_own_question"),
	
	/**
	 * to flag anything
	 */
	CREATE_FLAG("create_flag"),
	
	EDIT_QUESTION("edit_question"),
	EDIT_ANSWER("edit_answer"),

	/**
	 * to comment anything
	 */
	CREATE_COMMENT("create_comment"),

	MODERATE_EDITS("moderate_edits"),

	/**
	 * to interact with an inactive question 
	 */
	INACTIVATE_QUESTION("inactivate_question");

	private final String permissionName;

	PermissionRules(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionName() {
		return permissionName;
	}
}

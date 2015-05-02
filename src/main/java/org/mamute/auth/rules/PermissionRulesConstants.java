package org.mamute.auth.rules;

public class PermissionRulesConstants {
	/**
	 * to vote up anything
	 */
	public final static String VOTE_UP = "vote_up";

	/**
	 * to vote down anything
	 */
	public final static String VOTE_DOWN = "vote_down";
	
	/**
	 * to answer own question
	 */
	public final static String ANSWER_OWN_QUESTION = "answer_own_question";
	
	/**
	 * to flag anything
	 */
	public final static String CREATE_FLAG = "create_flag";
	
	public final static String EDIT_QUESTION = "edit_question";
	public final static String EDIT_ANSWER = "edit_answer";

	/**
	 * to comment anything
	 */
	public static final String CREATE_COMMENT = "create_comment";

	public final static String MODERATE_EDITS = "moderate_edits";

	/**
	 * to interact with an inactive question 
	 */
	public final static String INACTIVATE_QUESTION = "inactivate_question";
}

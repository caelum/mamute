package br.com.caelum.brutal.auth.rules;

public class PermissionRulesConstants {
	/**
	 * to vote up anything
	 */
	public final static int VOTE_UP = 10;
	
	/**
	 * to vote down anything
	 */
	public static final int VOTE_DOWN = 100;
	
	/**
	 * to answer own question
	 */
	public static final int ANSWER_OWN_QUESTION = 50;
	
	/**
	 * to flag anything
	 */
	public static final int CREATE_FLAG = 10;
	
	public static final int EDIT_QUESTION = 20;
	public static final int EDIT_ANSWER = 20;

	/**
	 * to comment anything
	 */
	public static final int CREATE_COMMENT = 0;

	public static final long MODERATE_EDITS = 500;
}

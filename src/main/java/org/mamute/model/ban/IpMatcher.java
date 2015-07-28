package org.mamute.model.ban;

public class IpMatcher {
	private final String[] fields;

	public IpMatcher(String expresison) {
		this.fields = expresison.split("\\.");
	}

	public boolean matches(String ip) {
		String[] ipFields = ip.split("\\.");
		return match(ipFields);
	}

	private boolean match(String[] ipFields) {
		return match(ipFields, 0) &&
			match(ipFields, 1) &&
			match(ipFields, 2) &&
			match(ipFields, 3);
	}

	private boolean match(String[] ipFields, int index) {
		String currentExpression = fields[index];
		return "*".equals(currentExpression) || currentExpression.equals(ipFields[index]);
	}
}

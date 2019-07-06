package org.mamute.list;

public class Tab {
	private final Type type;
	private final String localizationKey;
	private final String link;

	public Tab(Type type, String localizationKey, String link)
	{
		this.type = type;
		this.localizationKey = localizationKey;
		this.link = link;
	}

	public String getLocalizationKey() { return this.localizationKey; }
	public String getLink() { return this.link; }
	public Type getType() { return this.type; }

	public enum Type {
		VOTED("voted"), ANSWERED("answered"), VIEWED("viewed");

		private final String stringValue;

		Type(String stringValue) {
			this.stringValue = stringValue;
		}

		public String getStringValue() {
			return this.stringValue;
		}
	}
}

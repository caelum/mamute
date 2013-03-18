package br.com.caelum.brutal.reputation.rules;

public enum ReputationEvents implements KarmaRewardEvent {
	NEW_QUESTION {
		@Override
		public int reward() {
			return KarmaCalculator.ASKED_QUESTION;
		}
	};

	@Override
	public abstract int reward();

}

package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Votable;

public enum VoteType {
	UP(1) {
        @Override
        public int getKarmaValue(Class<? extends Votable> type) {
            return type.equals(Answer.class) ? 10 : 5;
        }
    },
    DOWN(-1) {
        @Override
        public int getKarmaValue(Class<? extends Votable> type) {
            return -2;
        }
    };

	private final int value;

	private VoteType(int value) {
		this.value = value;
	}

	public int getCountValue() {
		return value;
	}

    abstract public int getKarmaValue(Class<? extends Votable> type);
}

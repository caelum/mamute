package org.mamute.model;


public enum VoteType {
	UP(1){
		@Override
		public String toString() {
			return "UpVote";
		}
	},
	DOWN(-1) {
		@Override
		public String toString() {
			return "DownVote";
		}
	};

	private final int value;

	private VoteType(int value) {
		this.value = value;
	}

	public int getCountValue() {
		return value;
	}

}

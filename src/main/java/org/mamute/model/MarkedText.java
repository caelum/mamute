package org.mamute.model;

public class MarkedText {

	private String pure;
	private String marked;

	public static MarkedText pureAndMarked(String pure, String marked) {
		return new MarkedText(pure, marked);
	}

	public static MarkedText notMarked(String pure) {
		return new MarkedText(pure, pure);
	}

	private MarkedText(String pure, String marked) {
		this.pure = pure;
		this.marked = marked;
	}
	
	public String getPure() {
		return pure;
	}

	public String getMarked() {
		return marked;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((marked == null) ? 0 : marked.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MarkedText other = (MarkedText) obj;
		if (marked == null) {
			if (other.marked != null)
				return false;
		} else if (!marked.equals(other.marked))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MarkedText [pure=" + pure + ", marked=" + marked + "]";
	}


	
}

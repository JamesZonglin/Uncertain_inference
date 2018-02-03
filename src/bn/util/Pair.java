package bn.util;

public class Pair<X, Y> {
	private final X a;

	private final Y b;

	/**
	 * Constructs a Pair from two given elements
	 * 
	 * @param a
	 *            the first element
	 * @param b
	 *            the second element
	 */
	public Pair(X a, Y b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Returns the first element of the pair
	 * 
	 * @return the first element of the pair
	 */
	public X getFirst() {
		return a;
	}

	/**
	 * Returns the second element of the pair
	 * 
	 * @return the second element of the pair
	 */
	public Y getSecond() {
		return b;
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond().toString()
				+ " > ";
	}
}


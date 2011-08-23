package timezra.nist4j.randomness.tests;

import org.apache.commons.math.random.RandomGenerator;

public abstract class NISTTest {

	public abstract double pValue(final RandomGenerator e, final int n);

	public final boolean isRandom(final RandomGenerator e, final int n) {
		return pValue(e, n) >= 0.01;
	}

	protected final int ones(final RandomGenerator e, final int n) {
		int ones = 0;
		for (int i = 0; i < n; i++) {
			if (e.nextBoolean()) {
				ones++;
			}
		}
		return ones;
	}

	protected static interface FoldFunction<T> {
		T fun(boolean b, T accumulator);
	}

	protected final <T> T fold(final FoldFunction<T> f, final T accumulator,
			final RandomGenerator r, final int length) {
		T t = accumulator;
		for (int i = 0; i < length; i++) {
			final boolean b = r.nextBoolean();
			t = f.fun(b, t);
		}
		return t;
	}
}

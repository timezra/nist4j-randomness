package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.erfc;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of this test is the total number of runs in the sequence, where a
 * run is an uninterrupted sequence of identical bits. A run of length k
 * consists of exactly k identical bits and is bounded before and after with a
 * bit of the opposite value. The purpose of the runs test is to determine
 * whether the number of runs of ones and zeros of various lengths is as
 * expected for a random sequence. In particular, this test determines whether
 * the oscillation between such zeros and ones is too fast or too slow.
 */
public class Runs extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final int[] onesAndVn = onesAndVn(e, n);
		final int ones = onesAndVn[0];
		if (new Monobit().pValue(ones, n) < 0.01) {
			return 0.0;
		}
		final int doubleOnesAndZeroes = 2 * ones * (n - ones);
		final double pValue = erfc(new Fraction(onesAndVn[1] * n
				- doubleOnesAndZeroes, 2 * doubleOnesAndZeroes).abs()
				.doubleValue()
				* Math.sqrt(2 * n));
		return pValue;
	}

	private int[] onesAndVn(final RandomGenerator e, final int n) {
		int ones = 0;
		int vN = 1;
		if (n > 0) {
			boolean last = e.nextBoolean();
			if (last) {
				ones++;
			}
			for (int i = 1; i < n; i++) {
				final boolean next = e.nextBoolean();
				if (next) {
					ones++;
				}
				if (last != next) {
					vN++;
				}
				last = next;
			}
		}
		return new int[] { ones, vN };
	}
}

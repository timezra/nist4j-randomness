package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.igamc;

import java.math.BigInteger;
import java.util.Arrays;

import org.apache.commons.math.fraction.BigFraction;
import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of the test is the longest run of ones within M-bit blocks. The
 * purpose of this test is to determine whether the length of the longest run of
 * ones within the tested sequence is consistent with the length of the longest
 * run of ones that would be expected in a random sequence. Note that an
 * irregularity in the expected length of the longest run of ones implies that
 * there is also an irregularity in the expected length of the longest run of
 * zeroes. Therefore, only a test for ones is necessary.
 */
public class RunsOfOnes extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final State s = state(e, n);
		BigFraction xSquared = BigFraction.ZERO;
		for (int i = 0; i <= s.K; i++) {
			final BigFraction nPi = s.p[i].multiply(s.N);
			xSquared = xSquared.add(s.v[i].subtract(nPi).pow(2).divide(nPi));
		}
		return igamc(s.K / 2.0, xSquared.divide(BigInteger.valueOf(2))
				.doubleValue());
	}

	private State state(final RandomGenerator e, final int n) {
		if (n < 6272) {
			return new State_8(calculateRuns(e, n, 8));
		} else if (n < 750000) {
			return new State_128(calculateRuns(e, n, 128));
		} else {
			return new State_10000(calculateRuns(e, n, 10000));
		}
	}

	private int[] calculateRuns(final RandomGenerator e, final int n,
			final int M) {
		final int[] runs = new int[n / M];
		for (int i = 0; i < runs.length; i++) {
			runs[i] = longestRun(e, M);
		}
		return runs;
	}

	private int longestRun(final RandomGenerator e, final int blockSize) {
		int longestRun = 0;
		int currentRun = 0;
		for (int i = 0; i < blockSize; i++) {
			final boolean next = e.nextBoolean();
			if (next) {
				currentRun++;
				longestRun = Math.max(currentRun, longestRun);
			} else {
				currentRun = 0;
			}
		}
		return longestRun;
	}

	private static abstract class State {
		final int K;
		final BigInteger N;
		final BigFraction[] v;
		final BigFraction[] p;

		State(final int K, final BigInteger N, final BigFraction[] v,
				final BigFraction[] p) {
			this.K = K;
			this.N = N;
			this.v = v;
			this.p = p;
		}

		protected static BigFraction[] v(final int[] runs,
				final int lowerNormalBound, final int upperNormalBound) {
			final int highestIndex = upperNormalBound - lowerNormalBound;
			final BigFraction[] v = new BigFraction[highestIndex + 1];
			Arrays.fill(v, BigFraction.ZERO);
			for (int i = 0; i < runs.length; i++) {
				if (runs[i] <= lowerNormalBound) {
					v[0] = v[0].add(BigFraction.ONE);
				} else if (runs[i] >= upperNormalBound) {
					v[highestIndex] = v[highestIndex].add(BigFraction.ONE);
				} else {
					final int index = runs[i] - lowerNormalBound;
					v[index] = v[index].add(BigFraction.ONE);
				}
			}
			return v;
		}
	}

	private static final class State_8 extends State {
		private static final BigFraction[] PROBABILITIES_8;
		static {
			PROBABILITIES_8 = new BigFraction[] {
					new BigFraction(BigInteger.valueOf(55), BigInteger
							.valueOf(256)),
					new BigFraction(BigInteger.valueOf(47), BigInteger
							.valueOf(128)),
					new BigFraction(BigInteger.valueOf(59), BigInteger
							.valueOf(256)),
					new BigFraction(BigInteger.valueOf(3), BigInteger
							.valueOf(16)) };
		}

		State_8(final int[] runs) {
			super(3, BigInteger.valueOf(16), v(runs, 1, 4), PROBABILITIES_8);
		}
	}

	private static final class State_128 extends State {
		private static final BigFraction[] PROBABILITIES_128;
		static {
			PROBABILITIES_128 = new BigFraction[] {
					new BigFraction(new BigInteger(
							"9987591922978320627046659129766067565"),
							new BigInteger(
									"85070591730234615865843651857942052864")),
					new BigFraction(new BigInteger(
							"20668407220119869443755857676768490041"),
							new BigInteger(
									"85070591730234615865843651857942052864")),
					new BigFraction(new BigInteger(
							"84853996279826276250626470347266042761"),
							new BigInteger(
									"340282366920938463463374607431768211456")),
					new BigFraction(new BigInteger(
							"59609664725055171160800157421157640511"),
							new BigInteger(
									"340282366920938463463374607431768211456")),
					new BigFraction(new BigInteger(
							"1092105113395608223712259446020756341"),
							new BigInteger(
									"10633823966279326983230456482242756608")),
					new BigFraction(new BigInteger(
							"74701847099618735566300019852621279"),
							new BigInteger(
									"664613997892457936451903530140172288")) };
		}

		State_128(final int[] runs) {
			super(5, BigInteger.valueOf(49), v(runs, 4, 9), PROBABILITIES_128);
		}
	}

	private static final class State_10000 extends State {
		private static final BigFraction[] PROBABILITIES_10000;
		static {
			PROBABILITIES_10000 = new BigFraction[] {
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")),
					new BigFraction(new BigInteger(""), new BigInteger("")), };
		}

		State_10000(final int[] runs) {
			super(6, BigInteger.valueOf(75), v(runs, 10, 16),
					PROBABILITIES_10000);
		}
	}
}

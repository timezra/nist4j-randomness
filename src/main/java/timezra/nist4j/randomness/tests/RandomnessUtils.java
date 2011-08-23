package timezra.nist4j.randomness.tests;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.math.MathException;
import org.apache.commons.math.fraction.BigFraction;
import org.apache.commons.math.special.Erf;
import org.apache.commons.math.special.Gamma;
import org.apache.commons.math.util.MathUtils;

public final class RandomnessUtils {

	private RandomnessUtils() {
		// singleton
	}

	public static final double igamc(final double a, final double x) {
		try {
			return Gamma.regularizedGammaQ(a, x);
		} catch (final MathException e) {
			throw new RuntimeException(e);
		}
	}

	public static final double erfc(final double x) {
		try {
			return 1 - Erf.erf(x);
		} catch (final MathException e) {
			throw new RuntimeException(e);
		}
	}

	public static BigFraction[] probabilitiesOfLongestRunsOfOnes(
			final int blockSize, final int lower, final int upper) {
		final BigFraction[] ps = new BigFraction[upper - lower + 1];
		final TheoreticalProbabilitiesOfRunsOfOnesCalculator calculator = new TheoreticalProbabilitiesOfRunsOfOnesCalculator();
		ps[0] = calculator.p_le(blockSize, lower);
		BigFraction last_le = ps[0];
		for (int i = 1; i < ps.length - 1; i++) {
			final BigFraction this_le = calculator.p_le(blockSize, lower + i);
			ps[i] = this_le.subtract(last_le);
			last_le = this_le;
		}
		ps[ps.length - 1] = BigFraction.ONE.subtract(last_le);
		return ps;
	}

	public static int sum(final int[] is) {
		int sum = 0;
		for (final int i : is) {
			sum += i;
		}
		return sum;
	}

	public static BigDecimal e(final int i) {
		final Iterator<Integer> e = eIterator(10, i);
		final StringBuilder b = new StringBuilder("2.");
		while (e.hasNext()) {
			final int next = e.next().intValue();
			b.append(next);
		}
		return new BigDecimal(b.toString());
	}

	public static final Iterator<Integer> eIterator(final int base) {
		return new EIterator(base, Integer.MAX_VALUE);
	}

	public static final Iterator<Integer> eIterator(final int base,
			final int length) {
		return new EIterator(base, length);
	}

	public static final int rank(final boolean[][] a) {
		return RankCalculator.INSTANCE.rank(a);
	}

	public static final BigFraction[] rankProbabilities(final int M, final int Q) {
		return RankProbabilitiesCalculator.INSTANCE.calculateRankProbabilities(
				M, Q);
	}

	public static String[] aperiodicTemplates(final int length) {
		return templates(length, aperiodicValues(length));
	}

	public static String[] periodicTemplates(final int length) {
		return templates(length, periodicValues(length));
	}

	private static String[] templates(final int length, final int[] vs) {
		final String[] templates = new String[vs.length];
		for (int i = 0; i < templates.length; i++) {
			templates[i] = String.format("%1$#" + length + "s",
					Integer.toBinaryString(vs[i])).replaceAll("\\s", "0");
		}
		return templates;
	}

	/**
	 * B cannot be written as CC...CC' for a pattern C shorter than B with C'
	 * denoting a preÞx of C.
	 */
	public static final int[] aperiodicValues(final int length) {
		return partitionPeriodicOrAperiodcValues(length, false);
	}

	/**
	 * B can be written as CC...CC' for a pattern C shorter than B with C'
	 * denoting a preÞx of C.
	 */
	public static final int[] periodicValues(final int length) {
		return partitionPeriodicOrAperiodcValues(length, true);
	}

	private static final int[] partitionPeriodicOrAperiodcValues(
			final int length, final boolean isPeriodic) {
		if (length > 31) {
			throw new IllegalArgumentException(
					"The bit length of the aperiodic values must be less than 32.");
		}
		final int[] vs = new int[1 << length];
		int i = 0;
		for (int j = 0; j < vs.length; j++) {
			if (isPeriodic == isPeriodic(j, length)) {
				vs[i++] = j;
			}
		}
		final int[] partition = new int[i];
		System.arraycopy(vs, 0, partition, 0, i);
		return partition;
	}

	private static boolean isPeriodic(final int B, final int length) {
		OUTER: for (int i = 1; i < length; i++) {
			final int pattern = B >> length - i & (1 << i) - 1;
			for (int j = i + 1; j <= length; j += i) {
				final int matcher;
				final int toMatch;
				if (j + i - 1 <= length) {
					final int k = length - (j + i - 1);
					matcher = pattern;
					toMatch = B >> k & (1 << i) - 1;
				} else {
					final int Clength = length - j + 1;
					matcher = pattern >> i - Clength;
					toMatch = B & (1 << Clength) - 1;
				}
				if (toMatch != matcher) {
					continue OUTER;
				}
			}
			return true;
		}
		return false;
	}

	public static double probabilityOfOverlappingTemplate(final int u,
			final double eta) {
		double sum = 0;
		if (u == 0) {
			sum = 1;
		}
		for (int l = 1; l <= u; l++) {
			sum += Math.exp(MathUtils.binomialCoefficientLog(u - 1, l - 1) + l
					* Math.log(eta) - MathUtils.factorialLog(l));
		}
		return Math.exp(Math.log(sum) - eta - u * Math.log(2));
	}

	private static final class RankProbabilitiesCalculator {

		static final RankProbabilitiesCalculator INSTANCE = new RankProbabilitiesCalculator();

		private RankProbabilitiesCalculator() {
			// singleton
		}

		BigFraction[] calculateRankProbabilities(final int M, final int Q) {
			final BigFraction p[] = new BigFraction[Math.min(M, Q) + 1];
			final BigFraction TWO = new BigFraction(2);
			for (int r = 0; r < p.length; r++) {
				BigFraction product = TWO.pow(r * (Q + M - r) - M * Q);
				for (int i = 0; i < r; i++) {
					final BigFraction numerator = BigFraction.ONE.subtract(
							TWO.pow(i - Q)).multiply(
							BigFraction.ONE.subtract(TWO.pow(i - M)));
					final BigFraction denominator = BigFraction.ONE
							.subtract(TWO.pow(i - r));
					product = product.multiply(numerator).divide(denominator);
				}
				p[r] = product;
			}
			return p;
		}
	}

	private static final class RankCalculator {
		static final RankCalculator INSTANCE = new RankCalculator();

		private RankCalculator() {
			// singleton
		}

		int rank(final boolean[][] a) {
			makeTriangular(a);
			int rank = 0;
			for (int i = 0; i < a.length; i++) {
				if (a[i][i]) {
					rank++;
				}
			}
			return rank;
		}

		private void makeTriangular(final boolean[][] a) {
			makeUpperTriangular(a);
			makeLowerTriangular(a);
		}

		private void makeUpperTriangular(final boolean[][] a) {
			for (int i = 0; i < a.length - 1; i++) {
				if (a[i][i]) {
					for (int k = i + 1; k < a.length; k++) {
						if (a[k][i]) {
							xor(a, i, k);
						}
					}
				} else {
					for (int k = i + 1; k < a.length; k++) {
						if (a[k][i]) {
							swap(a, i, k);
							i--;
							break;
						}
					}
				}
			}
		}

		private void makeLowerTriangular(final boolean[][] a) {
			for (int i = a.length - 1; i > 0; i--) {
				if (a[i][i]) {
					for (int k = i - 1; k >= 0; k--) {
						if (a[k][i]) {
							xor(a, i, k);
						}
					}
				} else {
					for (int k = i - 1; k >= 0; k--) {
						if (a[k][i]) {
							swap(a, i, k);
							i++;
							break;
						}
					}
				}
			}
		}

		private void swap(final boolean[][] a, final int r1, final int r2) {
			final boolean[] swap = new boolean[a[r2].length];
			System.arraycopy(a[r2], 0, swap, 0, swap.length);
			a[r2] = a[r1];
			a[r1] = swap;
		}

		private void xor(final boolean[][] a, final int r1, final int r2) {
			for (int l = r1; l < a[r2].length; l++) {
				a[r2][l] ^= a[r1][l];
			}
		}
	}

	/**
	 * Based on the algorithm described by A. H. J. Sale in his paper <a
	 * href="http://eprints.utas.edu.au/121/1/Calculation_of_e.pdf">
	 * <em>The calculation of </em>e <em> to many significant digits</em></a>.
	 */
	private static final class EIterator implements Iterator<Integer> {

		private final int base;
		private final int length;
		private final int m;
		private final int coefs[];
		private int i;

		EIterator(final int base, final int length) {
			this.base = base;
			this.length = length;
			m = calculateM(length);
			coefs = new int[m + 1];
			Arrays.fill(coefs, 1);
		}

		private int calculateM(final int length) {
			int m = 4;
			final double test = (length + 1.0) * 2.30258509;
			while (m * (Math.log(m) - 1) + 0.5 * Math.log(2 * Math.PI * m) < test) {
				m++;
			}
			return m;
		}

		public boolean hasNext() {
			return i <= length;
		}

		public Integer next() {
			int next = 0;
			for (int j = m; j >= 2; j--) {
				final int temp = coefs[j] * base + next;
				next = temp / j;
				coefs[j] = temp % j;
			}
			i++;
			return next;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static final class TheoreticalProbabilitiesOfRunsOfOnesCalculator {

		private final Map<BigInteger, BigInteger> factorials;

		TheoreticalProbabilitiesOfRunsOfOnesCalculator() {
			factorials = new HashMap<BigInteger, BigInteger>();
			factorials.put(BigInteger.ZERO, BigInteger.ONE);
			factorials.put(BigInteger.ONE, BigInteger.ONE);
		}

		BigFraction p_le(final int M, final int m) {
			BigInteger sum = BigInteger.ZERO;
			for (int r = 0; r <= M; r++) {
				final int mMinusR = M - r;
				final int mPlus1 = m + 1;
				final int U = Math.min(mMinusR + 1, r / mPlus1);
				for (int j = 0; j <= U; j++) {
					final BigInteger product = choose(mMinusR + 1, j).multiply(
							choose(M - j * mPlus1, mMinusR));
					sum = j % 2 == 0 ? sum.add(product) : sum.subtract(product);
				}
			}
			return new BigFraction(sum, BigInteger.ONE.shiftLeft(M));
		}

		private BigInteger choose(final int n, final int k) {
			if (k > n) {
				return BigInteger.ZERO;
			}
			if (k == n || k == 0) {
				return BigInteger.ONE;
			}
			return cacheFactorial(BigInteger.valueOf(n)).divide(
					cacheFactorial(BigInteger.valueOf(k)).multiply(
							cacheFactorial(BigInteger.valueOf(n - k))));
		}

		private BigInteger cacheFactorial(final BigInteger b) {
			if (factorials.containsKey(b)) {
				return factorials.get(b);
			}
			final BigInteger factorial = b.multiply(cacheFactorial(b
					.subtract(BigInteger.ONE)));
			factorials.put(b, factorial);
			return factorial;
		}
	}
}

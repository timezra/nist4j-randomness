package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.igamc;
import static timezra.nist4j.randomness.tests.RandomnessUtils.rank;

import org.apache.commons.math.fraction.BigFraction;
import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of the test is the rank of disjoint sub-matrices of the entire
 * sequence. The purpose of this test is to check for linear dependence among
 * fixed length substrings of the original sequence. Note that this test also
 * appears in the DIEHARD battery of tests.
 */
public class BinaryMatrixRank extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final int[] MQN = calculateMQN(n);
		final int[] R = ranks(e, MQN[0], MQN[1], MQN[2]);
		final int[] F = countRanks(R, MQN[0]);
		final BigFraction[] p = rankProbabilities(MQN[0], MQN[1]);
		final BigFraction pFullN = p[0].multiply(MQN[2]);
		final BigFraction pFullMinus1N = p[1].multiply(MQN[2]);
		final BigFraction pTheRestN = p[2].multiply(MQN[2]);
		final BigFraction xSquared = new BigFraction(F[0])
				.subtract(pFullN)
				.pow(2)
				.divide(pFullN)
				.add(new BigFraction(F[1]).subtract(pFullMinus1N).pow(2)
						.divide(pFullMinus1N))
				.add(new BigFraction(F[2]).subtract(pTheRestN).pow(2)
						.divide(pTheRestN));
		return igamc(1, xSquared.divide(2).doubleValue());
	}

	private BigFraction[] rankProbabilities(final int M, final int Q) {
		final BigFraction[] p = RandomnessUtils.rankProbabilities(M, Q);
		return new BigFraction[] { p[M], p[M - 1],
				BigFraction.ONE.subtract(p[M]).subtract(p[M - 1]) };
	}

	private int[] countRanks(final int[] R, final int M) {
		final int[] F = new int[3];
		for (final int l : R) {
			if (l == M) {
				F[0]++;
			} else if (l == M - 1) {
				F[1]++;
			} else {
				F[2]++;
			}
		}
		return F;
	}

	private int[] ranks(final RandomGenerator e, final int M, final int Q,
			final int N) {
		final int[] ranks = new int[N];
		for (int i = 0; i < N; i++) {
			final boolean[][] a = new boolean[M][Q];
			for (int j = 0; j < a.length; j++) {
				for (int k = 0; k < a[j].length; k++) {
					a[j][k] = e.nextBoolean();
				}
			}
			ranks[i] = rank(a);
		}
		return ranks;
	}

	private int[] calculateMQN(final int n) {
		int discards = Integer.MAX_VALUE;
		final int[] MQN = new int[3];
		final int min = n > 341 ? 38 : 1;
		for (int N = n / 9; N >= min; N--) {
			final int M = (int) Math.sqrt(n / N);
			final int mod = n % (M * M * N);
			if (mod == 0) {
				MQN[0] = MQN[1] = M;
				MQN[2] = N;
				break;
			}
			if (mod < discards) {
				MQN[0] = MQN[1] = M;
				MQN[2] = N;
				discards = mod;
			}
		}
		return MQN;
	}
}

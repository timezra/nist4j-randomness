package timezra.nist4j.randomness.tests;

import static java.util.Arrays.binarySearch;

import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of this test is the length of a linear feedback shiftregister
 * (LFSR). The purpose of this test is to determine whether or not the sequence
 * is complex enough to be considered random. Random sequences are characterized
 * by longer LFSRs. An LFSR that is too short implies non- randomness.
 */
public class LinearComplexity extends NISTTest {

	private static final double[] Ts = { -2.5, -1.5, -0.5, 0.5, 1.5, 2.5 };
	private static final double[] π = { 0.01047, 0.03125, 0.125, 0.5, 0.25,
			0.0625, 0.020833 };

	@Override
	public double pValue(final RandomGenerator ε, final int n) {
		final int[] MN = calculateMN(n);
		final int M = MN[0];
		final int N = MN[1];
		final double µ = M / 2.0 + (M % 2 == 0 ? 8 : 10) / 36.0
				- (M / 3.0 + 2 / 9.0) / Math.pow(2, M);
		final int K = 6;
		final int[] v = new int[K + 1];
		for (int i = 0; i < N; i++) {
			final int L = linearComplexity(ε, M);
			v[T((M % 2 == 0 ? 1 : -1) * (L - µ) + 2.0 / 9.0)]++;
		}
		double χ = 0;
		for (int i = 0; i <= K; i++) {
			final double Nπ = N * π[i];
			χ += Math.pow((v[i] - Nπ), 2) / Nπ;
		}
		final double pValue = RandomnessUtils.igamc(K / 2.0, χ / 2);
		return pValue;
	}

	int T(final double T) {
		final int clazz = binarySearch(Ts, T);
		return clazz < 0 ? Math.abs(clazz) - 1 : clazz;
	}

	private int[] calculateMN(final int n) {
		int M;
		int N;
		int discards;
		final int minM;
		final int maxM;
		final int minN;
		if (n >= 100000) {
			minM = 500;
			maxM = 5000;
			minN = 200;
		} else {
			minM = 4;
			maxM = n / 2;
			minN = 2;
		}
		M = minM;
		N = n / M;
		discards = n % M;
		for (int nextM = M + 1, nextN = n / nextM, mod = n % nextM; nextM <= maxM
				&& nextN >= minN && discards > 0; nextM++, nextN = n / nextM, mod = n
				% nextM) {
			if (mod < discards) {
				M = nextM;
				N = nextN;
				discards = mod;
			}
		}
		return new int[] { 1000, 1000 };
	}

	int linearComplexity(final RandomGenerator ε, final int n) {
		int L = 0;
		final byte[] c = new byte[n];
		final byte[] b = new byte[n];
		final byte[] s = new byte[n];
		c[0] = 1;
		b[0] = 1;
		int m = -1;
		for (int N = 0; N < n; N++) {
			final byte sN = (byte) (ε.nextBoolean() ? 1 : 0);
			s[N] = sN;
			int d = sN;
			for (int i = 1; i <= L; i++) {
				d += c[i] * s[N - i];
			}
			d %= 2;
			if (d == 1) {
				final byte[] t = new byte[n];
				System.arraycopy(c, 0, t, 0, t.length);
				for (int i = 0; i < n - N + m; i++) {
					c[N - m + i] += b[i];
				}
				if (L <= N / 2) {
					L = N + 1 - L;
					m = N;
					System.arraycopy(t, 0, b, 0, b.length);
				}
			}
		}
		return L;
	}
}

package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.erfc;

import java.util.Arrays;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.transform.FastFourierTransformer;

/**
 * The focus of this test is the peak heights in the Discrete Fourier Transform
 * of the sequence. The purpose of this test is to detect periodic features
 * (i.e., repetitive patterns that are near each other) in the tested sequence
 * that would indicate a deviation from the assumption of randomness. The
 * intention is to detect whether the number of peaks exceeding the 95 %
 * threshold is significantly different than 5%.
 */
public class DiscreteFourierTransform extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final Complex[] S = dft(e, n);
		final double[] M = peaks(S);
		final double T = Math.sqrt(3 * n);
		final double N0 = 0.95 * n / 2;
		int N1 = 0;
		for (final double peak : M) {
			if (peak < T) {
				N1++;
			}
		}
		final double d = (N1 - N0) / Math.sqrt(n * 0.95 * 0.05 / 2);
		final double pValue = erfc(Math.abs(d) / Math.sqrt(2));
		return pValue;
	}

	private double modulus(final Complex c) {
		return c.abs();
	}

	private double[] peaks(final Complex[] S) {
		final double[] M = new double[S.length / 2];
		for (int i = 0; i < M.length; i++) {
			M[i] = modulus(S[i]);
		}
		return M;
	}

	private int nextHigherPowerOf2(final int n) {
		if (FastFourierTransformer.isPowerOf2(n)) {
			return n;
		}
		final String bits = String.format("%1$#32s", Integer.toBinaryString(n));
		final int i = bits.indexOf('1');
		return 1 << 32 - i;
	}

	private Complex[] dft(final RandomGenerator e, final int n) {
		// final double f[] = new double[nextHigherPowerOf2(n)];
		// for (int i = 0; i < n; i++) {
		// f[i] = e.nextBoolean() ? 1 : -1;
		// }
		// final Complex[] S = new Complex[n];
		// final Complex[] fft = new FastFourierTransformer().transform(f);
		// System.arraycopy(fft, 0, S, 0, S.length);
		// return S;

		// final int[] x = new int[n];
		// for (int i = 0; i < n; i++) {
		// x[i] = e.nextBoolean() ? 1 : -1;
		// }
		// final Complex[] f = new Complex[n];
		// for (int j = 0; j < n; j++) {
		// f[j] = Complex.ZERO;
		// for (int k = 1; k <= n; k++) {
		// f[j] = f[j].add(ComplexUtils.polar2Complex(1,
		// Math.PI * 2 * (k - 1) * j / 2).multiply(x[j]));
		// }
		// }
		// return f;
		final Complex[] f = new Complex[n];
		Arrays.fill(f, Complex.ZERO);
		final double twoPiOverN = 2 * Math.PI / n;
		for (int k = 0; k < n; k++) {
			final boolean next = e.nextBoolean();
			for (int j = 0; j < f.length; j++) {
				final double twoPiKJOverN = twoPiOverN * k * j;
				final Complex exp = new Complex(0, twoPiKJOverN).exp();
				f[j] = next ? f[j].add(exp) : f[j].subtract(exp);
			}
		}
		return f;
	}
}

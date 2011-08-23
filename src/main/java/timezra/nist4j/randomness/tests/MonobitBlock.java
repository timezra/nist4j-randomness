package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.igamc;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of the test is the proportion of ones within M-bit blocks. The
 * purpose of this test is to determine whether the frequency of ones in an
 * M-bit block is approximately M/2, as would be expected under an assumption of
 * randomness. For block size M=1, this test degenerates to test 1, the
 * Frequency (Monobit) test.
 */
public class MonobitBlock extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final int blockSize = (int) Math.sqrt(n);
		final int blockCount = n / blockSize;
		Fraction sum = Fraction.ZERO;
		for (int i = 0; i < blockCount; i++) {
			final Fraction p = new Fraction(ones(e, blockSize), blockSize);
			final Fraction f = p.subtract(Fraction.ONE_HALF);
			sum = f.multiply(f).add(sum);
		}
		final double xSquared = new Fraction(4).multiply(
				new Fraction(blockSize)).multiply(sum).doubleValue();
		final double pValue = igamc(blockCount / 2.0, xSquared / 2);
		return pValue;
	}
}

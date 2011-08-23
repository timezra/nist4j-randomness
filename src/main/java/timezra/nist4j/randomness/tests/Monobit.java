package timezra.nist4j.randomness.tests;

import static timezra.nist4j.randomness.tests.RandomnessUtils.erfc;

import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of the test is the proportion of zeroes and ones for the entire
 * sequence. The purpose of this test is to determine whether the number of ones
 * and zeros in a sequence are approximately the same as would be expected for a
 * truly random sequence. The test assesses the closeness of the fraction of
 * ones to 1/2, that is, the number of ones and zeroes in a sequence should be
 * about the same. All subsequent tests depend on the passing of this test;
 * there is no evidence to indicate that the tested sequence is non-random.
 */
public class Monobit extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		return pValue(ones(e, n), n);
	}

	double pValue(final int ones, final int n) {
		final int sum = (ones << 1) - n;
		final double pValue = erfc(Math.abs(sum)
				/ (Math.sqrt(2) * Math.sqrt(n)));
		return pValue;
	}
}

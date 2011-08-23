package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class DiscreteFourierTransformTest {

	private static final String A_RANDOM_SEQUENCE = "1100100100001111110110101010001000100001011010001100001000110100110001001100011001100010100010111000";
	// private static final String A_RANDOM_SEQUENCE = "1001010011";
	private static final String A_PERIODIC_SEQUENCE = "1101011010110101101011010110101101011010110101101011010110101101011010110101101011010110101101011010";
	private DiscreteFourierTransform discreteFourierTransform;

	@Before
	public void setup() {
		discreteFourierTransform = new DiscreteFourierTransform();
	}

	@Test
	public void thePValueIsTheErfcOfTheAbsoluteValueOfTheTestStatisticDividedByRoot2()
			throws Exception {
		final double pValue = discreteFourierTransform.pValue(
				RandomGenerators.from(A_RANDOM_SEQUENCE),
				A_RANDOM_SEQUENCE.length());
		assertEquals(0.330390, pValue, 0.0000005);
	}

	@Test
	public void ifTheSequenceIsPeriodicThenItIsNotRandom() throws Exception {
		assertFalse(discreteFourierTransform.isRandom(
				RandomGenerators.from(A_PERIODIC_SEQUENCE),
				A_PERIODIC_SEQUENCE.length()));
	}
}

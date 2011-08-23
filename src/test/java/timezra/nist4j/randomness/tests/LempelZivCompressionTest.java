package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LempelZivCompressionTest {

	private LempelZivCompression lempelZivCompression;

	@Before
	public void setup() {
		lempelZivCompression = new LempelZivCompression();
	}

	@Test
	public void thePValueDeterminesHowFarTheSequenceCanBeCompressed()
			throws Exception {
		final int length = 1000000;
		assertEquals(
				0.000584,
				lempelZivCompression.pValue(RandomGenerators.e(length), length),
				0.0000005);
	}

	@Test
	public void ifTheSequenceCanBeSignificantlyCompressedThenItIsNotRandom()
			throws Exception {
		final int length = 1000000;
		assertFalse(lempelZivCompression.isRandom(RandomGenerators.e(length),
				length));
	}

	// FIXME: Need to create a random number generator that ensures large word
	// length
	@Test
	public void ifTheSequenceCannotBeSignificantlyCompressedThenItMightBeRandom()
			throws Exception {
		final int length = 1000000;
		assertTrue(lempelZivCompression.isRandom(RandomGenerators.e(length),
				length));
	}
}

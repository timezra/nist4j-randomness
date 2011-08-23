package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;

public class OverlappingTemplateMatcherTest {

	private OverlappingTemplateMatcher overlappingTemplateMatcher;

	@Test
	public void thePvalueIsBasedOnTheNumberOfOccurencesOfTheNonOverlappingTemplateInEachWindow()
			throws Exception {
		overlappingTemplateMatcher = new OverlappingTemplateMatcher(3, 2);
		final String randomSequence = "10111011110110110100011100101110111110000101101001";
		final double pValue = overlappingTemplateMatcher.pValue(
				RandomGenerators.from(randomSequence), randomSequence.length());
		assertEquals(0.0688213485, pValue, 0.00000000005);
	}

	@Test
	public void ifAPeriodicPatternAppearsMoreOftenThanExpectedThenTheSequenceIsNotRandom()
			throws Exception {
		overlappingTemplateMatcher = new OverlappingTemplateMatcher();
		final String periodicSequence = "01010101010101010000000000000000000000";
		assertFalse(overlappingTemplateMatcher.isRandom(
				RandomGenerators.from(periodicSequence),
				periodicSequence.length()));
	}

	@Test
	public void ifNoPeriodicPatternAppearsMoreOftenThanExpectedThenTheSequenceMightBeRandom()
			throws Exception {
		overlappingTemplateMatcher = new OverlappingTemplateMatcher(0x1FF, 9);
		final int n = 10000;
		final RandomGenerator e = RandomGenerators.e(n);
		assertTrue(overlappingTemplateMatcher.isRandom(e, n));
	}
}

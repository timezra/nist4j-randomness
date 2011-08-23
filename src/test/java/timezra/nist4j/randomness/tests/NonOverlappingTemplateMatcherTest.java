package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NonOverlappingTemplateMatcherTest {

	private NonOverlappingTemplateMatcher nonOverlappingTemplateMatcher;

	@Test
	public void thePvalueIsBasedOnTheNumberOfOccurencesOfTheNonOverlappingTemplateInEachWindow()
			throws Exception {
		nonOverlappingTemplateMatcher = new NonOverlappingTemplateMatcher(1, 3);
		final String randomSequence = "10100100101110010110";
		final double pValue = nonOverlappingTemplateMatcher.pValue(
				RandomGenerators.from(randomSequence), randomSequence.length());
		assertEquals(0.344154, pValue, 0.0000005);
	}

	@Test
	public void ifAnAperiodicPatternAppearsMoreOftenThanExpectedThenTheSequenceIsNotRandom()
			throws Exception {
		nonOverlappingTemplateMatcher = new NonOverlappingTemplateMatcher();
		final String periodicSequence = "00000000100000000010000000000000000000";
		assertFalse(nonOverlappingTemplateMatcher.isRandom(
				RandomGenerators.from(periodicSequence),
				periodicSequence.length()));
	}

	@Test
	public void ifNoAperiodicPatternAppearsMoreOftenThanExpectedThenTheSequenceMightBeRandom()
			throws Exception {
		nonOverlappingTemplateMatcher = new NonOverlappingTemplateMatcher();
		final String aperiodicSequence = "00000000000000000000000000000000000000";
		assertTrue(nonOverlappingTemplateMatcher.isRandom(
				RandomGenerators.from(aperiodicSequence),
				aperiodicSequence.length()));
	}
}

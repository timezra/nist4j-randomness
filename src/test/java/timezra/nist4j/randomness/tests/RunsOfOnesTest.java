package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static timezra.nist4j.randomness.tests.RandomGenerators.distribute;
import static timezra.nist4j.randomness.tests.RandomGenerators.oscillate;

import org.junit.Before;
import org.junit.Test;

public class RunsOfOnesTest {

	private static final String A_RANDOM_SEQUENCE = "11001100000101010110110001001100111000000000001001001101010100010001001111010110100000001101011111001100111001101101100010110010";
	private RunsOfOnes runsOfOnes;

	@Before
	public void setup() {
		runsOfOnes = new RunsOfOnes();
	}

	@Test
	public void thePValueIsTheIGAMCOfTheTestStatistic() throws Exception {
		final double pValue = runsOfOnes.pValue(RandomGenerators
				.from(A_RANDOM_SEQUENCE), A_RANDOM_SEQUENCE.length());
		assertEquals(0.180609, pValue, 0.0000005);
	}

	@Test
	public void ifTheDistributionOfTheLongestRunsOfOnesInASequenceShorterThan6272BitsIsNotNormalThenTheSequenceIsNotRandom()
			throws Exception {
		assertFalse(runsOfOnes.isRandom(oscillate(128, 2), 128));
	}

	@Test
	public void ifTheDistributionOfTheLongestRunsOfOnesInASequenceLongerThan6272BitsButShorterThan750000BitsIsNotNormalThenTheSequenceIsNotRandom()
			throws Exception {
		assertFalse(runsOfOnes.isRandom(oscillate(8192, 2), 8192));
	}

	@Test
	public void ifTheDistributionOfTheLongestRunsOfOnesInASequenceShorterThan6272BitsIsNormalThenTheSequenceMightBeRandom()
			throws Exception {
		assertTrue(runsOfOnes.isRandom(distribute(1, 2, 5, 3, 2, 1, 1, 1, 0),
				128));
	}

	@Test
	public void ifTheDistributionOfTheLongestRunsOfOnesInASequenceLongerThan6272BitsButShorterThan750000BitsIsNormalThenTheSequenceMightBeRandom()
			throws Exception {
		assertTrue( //
		runsOfOnes.isRandom(distribute(1, 1, 1, 2, 2, 16, 16, 10, 7, 1, 1, 0,
				0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), 8192));
	}
}

package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static timezra.nist4j.randomness.tests.RandomGenerators.oscillate;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class RunsTest {

	private static final String A_RANDOM_SEQUENCE = "1100100100001111110110101010001000100001011010001100001000110100110001001100011001100010100010111000";
	private Runs runs;

	@Before
	public void setup() {
		runs = new Runs();
	}

	@Test
	public void thePValueIsTheERFCOfTheTestStatistic() throws Exception {
		final double pValue = runs.pValue(RandomGenerators
				.from(A_RANDOM_SEQUENCE), A_RANDOM_SEQUENCE.length());
		assertEquals(0.500798, pValue, 0.0000005);
	}

	@Test
	public void thePValueIsZeroIfTheMonbitTestFails() throws Exception {
		final BitSet e = new BitSet(100);
		e.set(0, 63, true);
		e.set(64, 100, false);
		final double pValue = runs.pValue(RandomGenerators.from(e), 100);
		assertEquals(0.0, pValue, 0.0);
	}

	@Test
	public void theSequenceIsNotRandomIfTheOscillationBetweenOnesAndZeroesIsTooSlow()
			throws Exception {
		assertFalse(runs.isRandom(oscillate(100, 3), 100));
	}

	@Test
	public void theSequenceIsNotRandomIfTheOscillationsBetweenOnesAndZeroesIsTooFast()
			throws Exception {
		assertFalse(runs.isRandom(oscillate(100, 1), 100));
	}

	@Test
	public void theSequenceMayBeRandomIfTheOscillationsBetweenOnesAndZeroesIsNotTooFastOrTooSlow()
			throws Exception {
		assertTrue(runs.isRandom(oscillate(100, 2), 100));
	}
}

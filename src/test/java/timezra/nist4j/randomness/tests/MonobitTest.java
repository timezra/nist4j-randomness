package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class MonobitTest {

	private static final String A_RANDOM_SEQUENCE = "1100100100001111110110101010001000100001011010001100001000110100110001001100011001100010100010111000";
	private Monobit monobit;

	@Before
	public void setup() {
		monobit = new Monobit();
	}

	@Test
	public void thePValueIsTheERFCOfTheTestStatistic() throws Exception {
		final double pValue = monobit.pValue(RandomGenerators
				.from(A_RANDOM_SEQUENCE), A_RANDOM_SEQUENCE.length());
		assertEquals(0.109599, pValue, 0.0000005);
	}

	@Test
	public void theMonobitTestFailsForTooManyOnes() throws Exception {
		final BitSet e = new BitSet(100);
		e.set(0, 63, true);
		e.set(64, 100, false);
		assertFalse(monobit.isRandom(RandomGenerators.from(e), 100));
	}

	@Test
	public void theMonobitTestFailsForTooManyZeroes() throws Exception {
		final BitSet e = new BitSet(100);
		e.set(0, 63, false);
		e.set(64, 100, true);
		assertFalse(monobit.isRandom(RandomGenerators.from(e), 100));
	}

	@Test
	public void theMonobitTestSucceedsIfTheNumberOfOnesAndZeroesIsClose()
			throws Exception {
		final BitSet e = new BitSet(100);
		e.set(0, 62, true);
		e.set(63, 100, false);
		assertTrue(monobit.isRandom(RandomGenerators.from(e), 100));
	}
}

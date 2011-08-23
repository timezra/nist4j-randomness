package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;

public class MonobitBlockTest {

	private static final String A_RANDOM_SEQUENCE = "1100100100001111110110101010001000100001011010001100001000110100110001001100011001100010100010111000";
	private MonobitBlock monobitBlock;

	@Before
	public void setup() {
		monobitBlock = new MonobitBlock();
	}

	@Test
	public void thePValueIsTheIGAMCOfHalfTheBlockCountAndHalfTheXSquaredDistributionOfOnesInTheBlocks()
			throws Exception {
		final double pValue = monobitBlock.pValue(RandomGenerators
				.from(A_RANDOM_SEQUENCE), A_RANDOM_SEQUENCE.length());
		assertEquals(0.706438, pValue, 0.0000005);
	}

	@Test
	public void theMonobitBlockTestFailsForTooManyOnesInAGivenBlock()
			throws Exception {
		final BitSet e = new BitSet(100);
		for (int i = 0; i < 100; i++) {
			e.set(i, i % 4 != 0);
		}
		assertFalse(monobitBlock.isRandom(RandomGenerators.from(e), 100));
	}

	@Test
	public void theMonobitBlockTestFailsForTooManyZeroesInAGivenBlock()
			throws Exception {
		final BitSet e = new BitSet(100);
		for (int i = 0; i < 100; i++) {
			e.set(i, i % 4 == 0);
		}
		assertFalse(monobitBlock.isRandom(RandomGenerators.from(e), 100));
	}

	@Test
	public void theMonobitBlockTestSucceedsIfTheNumberOfOnesAndZeroesInEachBlockIsClose()
			throws Exception {
		final BitSet e = new BitSet(100);
		for (int i = 0; i < 100; i++) {
			e.set(i, i % 2 == 0);
		}
		assertTrue(monobitBlock.isRandom(RandomGenerators.from(e), 100));
	}
}

package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class BinaryMatrixRankTest {
	private static final String A_RANDOM_SEQUENCE = "01011001001010101101";
	private BinaryMatrixRank binaryMatrixRank;

	@Before
	public void setup() {
		binaryMatrixRank = new BinaryMatrixRank();
	}

	@Test
	public void thePValueIsTheIGAMCOfHalfTheTestStatistic() throws Exception {
		final double pValue = binaryMatrixRank.pValue(
				RandomGenerators.from(A_RANDOM_SEQUENCE),
				A_RANDOM_SEQUENCE.length());
		assertEquals(0.8209616256861868, pValue, Double.MIN_VALUE);
	}

	@Test
	public void ifTheBinaryMatrixRanksAreNotCloseToTheMatrixSizeThenTheSequenceIsNotRandom()
			throws Exception {
		final RandomGenerator e = RandomGenerators
				.from("11001100110011001000010010000100");
		assertFalse(binaryMatrixRank.isRandom(e, 32));
	}

	@Test
	public void ifTheBinaryMatrixRanksAreMostlyCloseToTheMatrixSizeThenTheSequenceMightBeRandom()
			throws Exception {
		final RandomGenerator e = RandomGenerators.e(100000);
		assertTrue(binaryMatrixRank.isRandom(e, 100000));
	}
}

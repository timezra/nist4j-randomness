package timezra.nist4j.randomness.tests;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.math.fraction.BigFraction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ProbabilitiesOfLongestRunsOfOnesParameterizedTest {

	private final int blockSize;
	private final int lower;
	private final int upper;
	private final double[] expectedProbabilities;

	@Parameters
	public static List<Object[]> parameters() {
		return asList(new Object[][] { //
				{ 8, 1, 4, new double[] { 0.2148, 0.3672, 0.2305, 0.1875 } },
				{
						128,
						4,
						9,
						new double[] { 0.1174, 0.2430, 0.2493, 0.1752, 0.1027,
								0.1124 } },
		// {
		// 512,
		// 6,
		// 11,
		// new double[] { 0.1170, 0.2460, 0.2523, 0.1755, 0.1015,
		// 0.1077 } },
		// {
		// 1000,
		// 7,
		// 16,
		// new double[] { 0.1307, 0.2437, 0.2452, 0.1714, 0.1002,
		// 0.1088, 0.0882, 0.2092, 0.2483, 0.1933, 0.1208,
		// 0.0675, 0.0727 } }
		});
	}

	public ProbabilitiesOfLongestRunsOfOnesParameterizedTest(final int blockSize,
			final int lower, final int upper,
			final double[] expectedProbabilities) {
		this.blockSize = blockSize;
		this.lower = lower;
		this.upper = upper;
		this.expectedProbabilities = expectedProbabilities;
	}

	@Test
	public void verifyExpectedTheoreticalProbabilities() throws Exception {
		final BigFraction[] ps = RandomnessUtils
				.probabilitiesOfLongestRunsOfOnes(blockSize, lower, upper);
		assertEquals(expectedProbabilities.length, ps.length);
		for (int i = 0; i < expectedProbabilities.length; i++) {
			assertEquals(expectedProbabilities[i], ps[i].doubleValue(), 0.0005);
		}
	}
}

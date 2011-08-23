package timezra.nist4j.randomness.tests;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ProbabilitiesOfOverlappingTemplatesParameterizedTest {

	private final int u;
	private final double eta;
	private final double expectedProbability;
	private final double epsilon;

	@Parameters
	public static List<Object[]> parameters() {
		return asList(new Object[][] { //
		{ 0, 1, 0.367879, 0.0000005 }, //
				{ 1, 1, 0.183940, 0.0000005 }, //
				{ 2, 1, 0.137955, 0.0000005 }, //
				{ 3, 1, 0.099634, 0.0000005 }, //
				{ 4, 1, 0.069935, 0.0000005 }, //
				{ 5, 1, 0.047997, 0.0000005 }, //
				{ 0, 1.125, 0.324652, 0.0000005 }, //
				{ 1, 1.125, 0.182617, 0.0000005 }, //
				{ 2, 1.125, 0.142670, 0.0000005 }, //
				{ 3, 1.125, 0.106645, 0.0000005 }, //
				{ 4, 1.125, 0.077147, 0.0000005 }, //
				{ 5, 1.125, 0.054400, 0.0000005 }, //
		});
	}

	public ProbabilitiesOfOverlappingTemplatesParameterizedTest(final int u,
			final double eta, final double expectedProbability,
			final double epsilon) {
		this.u = u;
		this.eta = eta;
		this.expectedProbability = expectedProbability;
		this.epsilon = epsilon;
	}

	@Test
	public void verifyExpectedTheoreticalProbabilities() throws Exception {
		final double actualProbability = RandomnessUtils
				.probabilityOfOverlappingTemplate(u, eta);
		assertEquals(expectedProbability, actualProbability, epsilon);
	}
}

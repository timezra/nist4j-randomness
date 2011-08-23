package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LinearComplexityTest {

	private LinearComplexity linearComplexity;

	@Before
	public void setup() {
		linearComplexity = new LinearComplexity();
	}

	@Test
	public void theBerlekampMasseyAlgorithmDeterminesTheLinearComplexity()
			throws Exception {
		final int L = linearComplexity.linearComplexity(
				RandomGenerators.from("001101110"), 9);
		assertEquals(5, L);
	}

	@Test
	public void thePValueIsDeterminedByTheLinearComplexity() throws Exception {
		final int length = 1000000;
		final double pValue = linearComplexity.pValue(
				RandomGenerators.e(length), length);
		assertEquals(0.845406, pValue, 0.0000005);
	}

	@Test
	public void thereAre7ComplexityClasses() throws Exception {
		assertEquals(0, linearComplexity.T(-2.6));
		assertEquals(0, linearComplexity.T(-2.5));
		assertEquals(1, linearComplexity.T(-1.6));
		assertEquals(1, linearComplexity.T(-1.5));
		assertEquals(2, linearComplexity.T(-0.6));
		assertEquals(2, linearComplexity.T(-0.5));
		assertEquals(3, linearComplexity.T(0.4));
		assertEquals(3, linearComplexity.T(0.5));
		assertEquals(4, linearComplexity.T(1.4));
		assertEquals(4, linearComplexity.T(1.5));
		assertEquals(5, linearComplexity.T(2.4));
		assertEquals(5, linearComplexity.T(2.5));
		assertEquals(6, linearComplexity.T(2.6));
	}
}

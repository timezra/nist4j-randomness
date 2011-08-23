package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.random.RandomGenerator;
import org.junit.Test;

public class RandomGeneratorsTest {

	@Test
	public void eCanBeAdaptedToBeARandomGenerator() throws Exception {
		final int decimalPlaces = 51;
		final RandomGenerator generator = RandomGenerators.e(decimalPlaces);
		double e = 2;
		for (int i = 1; i <= decimalPlaces; i++) {
			if (generator.nextBoolean()) {
				e += 1.0 / (1l << i);
			}
		}
		assertEquals(Math.E, e, Double.MIN_VALUE);
	}

	@Test
	public void iteratingWithABufferIsFaster() throws Exception {
		final int length = 100;
		final RandomGenerator buffer16 = RandomGenerators.e(length, 10);
		for (int i = 0; i < length; i++) {
			buffer16.nextBoolean();
		}
	}

	@Test
	public void eCanBeCalculatedInABuffer() throws Exception {
		final int length = 100000;
		final RandomGenerator buffer16 = RandomGenerators.e(length, 16);
		final RandomGenerator buffer2 = RandomGenerators.e(length, 1);
		for (int i = 0; i < length; i++) {
			assertEquals("failed at index = " + i, buffer2.nextBoolean(),
					buffer16.nextBoolean());
		}
	}
}

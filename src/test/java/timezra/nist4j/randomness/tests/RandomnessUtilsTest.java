package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static timezra.nist4j.randomness.tests.RandomnessUtils.probabilitiesOfLongestRunsOfOnes;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.commons.math.fraction.BigFraction;
import org.junit.Ignore;
import org.junit.Test;

public class RandomnessUtilsTest {

	@Test
	public void theProbabilitiesSumToOneForClass1() throws Exception {
		final BigFraction[] ps = probabilitiesOfLongestRunsOfOnes(8, 1, 4);
		assertEquals(4, ps.length);
		final BigFraction sum = ps[0].add(ps[1]).add(ps[2]).add(ps[3]);
		final double doubleValue = sum.doubleValue();
		assertEquals(1.0, doubleValue, 0.00005);
	}

	@Test
	public void theProbabilitiesSumToOneForClass2() throws Exception {
		final BigFraction[] ps = probabilitiesOfLongestRunsOfOnes(128, 4, 9);
		assertEquals(6, ps.length);
		final BigFraction sum = ps[0].add(ps[1]).add(ps[2]).add(ps[3])
				.add(ps[4]).add(ps[5]);
		assertEquals(1.0, sum.doubleValue(), 0.00005);
	}

	@Test
	public void theProbabilitiesSumToOneForClass2a() throws Exception {
		final BigFraction[] ps = probabilitiesOfLongestRunsOfOnes(512, 6, 11);
		assertEquals(6, ps.length);
		final BigFraction sum = ps[0].add(ps[1]).add(ps[2]).add(ps[3])
				.add(ps[4]).add(ps[5]);
		assertEquals(1.0, sum.doubleValue(), 0.00005);
	}

	@Test
	@Ignore
	public void theProbabilitiesSumToOneForClass2b() throws Exception {
		final BigFraction[] ps = probabilitiesOfLongestRunsOfOnes(1000, 7, 12);
		assertEquals(6, ps.length);
		final BigFraction sum = ps[0].add(ps[1]).add(ps[2]).add(ps[3])
				.add(ps[4]).add(ps[5]);
		assertEquals(1.0, sum.doubleValue(), 0.00005);
	}

	@Test
	@Ignore
	public void theProbabilitiesSumToOneForClass3() throws Exception {
		final BigFraction[] ps = probabilitiesOfLongestRunsOfOnes(10000, 10, 16);
		assertEquals(7, ps.length);
		final BigFraction sum = ps[0].add(ps[1]).add(ps[2]).add(ps[3])
				.add(ps[4]).add(ps[5]).add(ps[6]);
		assertEquals(1.0, sum.doubleValue(), 0.00005);
		for (final BigFraction p : ps) {
			System.out.println(p.reduce());
		}
	}

	@Test
	public void eCanBeCalculatedToAnyNumberOfDecimalPlaces() throws Exception {
		final int precision = 100000;
		final BigDecimal e = RandomnessUtils.e(precision);
		assertEquals(Math.E, e.doubleValue(), 0.0);
	}

	@Test
	public void rankCanBeCalculatedForABinaryMatrix() throws Exception {
		final boolean[][] matrix = new boolean[][] {
				{ true, false, false, false, false, false },
				{ false, false, false, false, false, true },
				{ true, false, false, false, false, true },
				{ true, false, true, false, true, false },
				{ false, false, true, false, true, true },
				{ false, false, false, false, true, false } };
		assertEquals(4, RandomnessUtils.rank(matrix));
	}

	@Test
	public void rankProbabilitiesCanBeCalculatedForBinaryMatrices()
			throws Exception {
		final int M = 32;
		final BigFraction[] p = RandomnessUtils.rankProbabilities(M, M);
		for (int i = 0; i < p.length - 3; i++) {
			assertTrue(p[i].bigDecimalValue(3, BigDecimal.ROUND_HALF_UP)
					.doubleValue() <= 0.005);
		}
		assertEquals(0.1284,
				p[M - 2].bigDecimalValue(4, BigDecimal.ROUND_HALF_UP)
						.doubleValue(), 0.00005);
		assertEquals(0.5776,
				p[M - 1].bigDecimalValue(4, BigDecimal.ROUND_HALF_UP)
						.doubleValue(), 0.00005);
		assertEquals(0.2888, p[M].bigDecimalValue(4, BigDecimal.ROUND_HALF_UP)
				.doubleValue(), 0.00005);
	}

	@Test(expected = IllegalArgumentException.class)
	public void onlyAperiodicValuesWithBitLengthLessThan32CanBeCalculated()
			throws Exception {
		RandomnessUtils.aperiodicValues(32);
	}

	@Test
	public void testname() throws Exception {
		final Iterator<Integer> e = RandomnessUtils.eIterator(10, 20);
		final StringBuilder b = new StringBuilder(20).append("2.");
		while (e.hasNext()) {
			final Integer next = e.next();
			b.append(next);
		}
		System.out.println(Math.E);
		System.out.println(b.toString());
	}
}

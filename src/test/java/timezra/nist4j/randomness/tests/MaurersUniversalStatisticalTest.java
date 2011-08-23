package timezra.nist4j.randomness.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MaurersUniversalStatisticalTest {

	private MaurersUniversalStatistical maurersUniversalStatistical;

	@Before
	public void setup() {
		maurersUniversalStatistical = new MaurersUniversalStatistical();
	}

	@Test
	public void theTestStatisticCalculatesTheDistanceBetweenMatchingPatterns()
			throws Exception {
		final String randomSequence = "01011010011101010111";
		final double pValue = maurersUniversalStatistical.pValue(
				RandomGenerators.from(randomSequence), randomSequence.length());
		assertEquals(0.767189, pValue, 0.0000005);
	}

	@Test
	public void ifTheSequenceIsSignificantlyCompressibleThenItIsNotRandom()
			throws Exception {
		final int length = 387840;
		final RandomGenerator generator = RandomGenerators.oscillate(length,
				length / 60);
		assertFalse(maurersUniversalStatistical.isRandom(generator, length));
	}

	@Test
	public void ifTheSequenceIsNotSignificantlyCompressibleThenItMightBeRandom()
			throws Exception {
		final int length = 387840;
		final RandomGenerator generator = RandomGenerators.e(length);
		assertTrue(maurersUniversalStatistical.isRandom(generator, length));
	}

	@Test
	public void findBlockSize() throws Exception {
		assertEquals(2, maurersUniversalStatistical.blockSize(24239));
		assertEquals(3, maurersUniversalStatistical.blockSize(24240));
		assertEquals(3, maurersUniversalStatistical.blockSize(64639));
		assertEquals(4, maurersUniversalStatistical.blockSize(64640));
		assertEquals(4, maurersUniversalStatistical.blockSize(161599));
		assertEquals(5, maurersUniversalStatistical.blockSize(161600));
		assertEquals(5, maurersUniversalStatistical.blockSize(387839));
		assertEquals(6, maurersUniversalStatistical.blockSize(387840));
		assertEquals(6, maurersUniversalStatistical.blockSize(904959));
		assertEquals(7, maurersUniversalStatistical.blockSize(904960));
		assertEquals(7, maurersUniversalStatistical.blockSize(2068479));
		assertEquals(8, maurersUniversalStatistical.blockSize(2068480));
		assertEquals(8, maurersUniversalStatistical.blockSize(4654079));
		assertEquals(9, maurersUniversalStatistical.blockSize(4654080));
		assertEquals(9, maurersUniversalStatistical.blockSize(10342399));
		assertEquals(10, maurersUniversalStatistical.blockSize(10342400));
		assertEquals(10, maurersUniversalStatistical.blockSize(22753279));
		assertEquals(11, maurersUniversalStatistical.blockSize(22753280));
		assertEquals(11, maurersUniversalStatistical.blockSize(49643519));
		assertEquals(12, maurersUniversalStatistical.blockSize(49643520));
		assertEquals(12, maurersUniversalStatistical.blockSize(107560959));
		assertEquals(13, maurersUniversalStatistical.blockSize(107560960));
		assertEquals(13, maurersUniversalStatistical.blockSize(231669759));
		assertEquals(14, maurersUniversalStatistical.blockSize(231669760));
		assertEquals(14, maurersUniversalStatistical.blockSize(496435199));
		assertEquals(15, maurersUniversalStatistical.blockSize(496435200));
		assertEquals(15, maurersUniversalStatistical.blockSize(1059061759));
		assertEquals(16, maurersUniversalStatistical.blockSize(1059061760));
		assertEquals(16, maurersUniversalStatistical.blockSize(1059061761));
	}
}

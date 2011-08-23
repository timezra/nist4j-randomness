package timezra.nist4j.randomness.tests;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static java.util.Arrays.binarySearch;
import static org.apache.commons.math.util.MathUtils.log;
import static timezra.nist4j.randomness.tests.RandomnessUtils.erfc;

import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of this test is the number of bits between matching patterns (a
 * measure that is related to the length of a compressed sequence). The purpose
 * of the test is to detect whether or not the sequence can be significantly
 * compressed without loss of information. A significantly compressible sequence
 * is considered to be non-random.
 */
public class MaurersUniversalStatistical extends NISTTest {

	private static final int[] PARTITION_BOUNDS = { 24240, 64640, 161600,
			387840, 904960, 2068480, 4654080, 10342400, 22753280, 49643520,
			107560960, 231669760, 496435200, 1059061760 };

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final Partition p = createPartition(n);
		final int[] t = new int[1 << p.blockSize];
		// initialization sequence
		for (int i = 1; i <= p.initialBlocks; i++) {
			int j = 0;
			for (int k = 0; k < p.blockSize; k++) {
				j <<= 1;
				j |= e.nextBoolean() ? 1 : 0;
			}
			t[j] = i;
		}
		// calculate the sum
		double sum = 0;
		for (int i = p.initialBlocks + 1; i <= p.testBlocks + p.initialBlocks; i++) {
			int j = 0;
			for (int k = 0; k < p.blockSize; k++) {
				j <<= 1;
				j |= e.nextBoolean() ? 1 : 0;
			}
			final int distance = i - t[j];
			t[j] = i;
			sum += log(2, distance);
		}
		return erfc(abs((sum / p.testBlocks - p.mean)
				/ (sqrt(p.variance) * sqrt(2))));
	}

	int blockSize(final int n) {
		final int index = binarySearch(PARTITION_BOUNDS, n);
		return index < 0 ? -index + 1 : index + 3;
	}

	private Partition createPartition(final int n) {
		final int blockSize = blockSize(n);
		final int initialBlocks = n < 100 ? n / 5 : 10 * (1 << blockSize);
		final int testBlocks = n / blockSize - initialBlocks;
		return new Partition(blockSize, initialBlocks, testBlocks);
	}

	private static final class Partition {

		private static final double[] MEANS = { Double.NaN, 0.7326495,
				1.5374383, 2.4016068, 3.3112247, 4.2534266, 5.2177052,
				6.1962507, 7.1836656, 8.1764248, 9.1723243, 10.170032,
				11.168765, 12.168070, 13.167693, 14.167488, 15.167379 };

		private static final double[] VARIANCES = { Double.NaN, 0.690, 1.338,
				1.901, 2.358, 2.705, 2.954, 3.125, 3.238, 3.311, 3.356, 3.384,
				3.401, 3.410, 3.416, 3.419, 3.421 };

		final int blockSize;
		final int initialBlocks;
		final int testBlocks;
		final double mean;
		final double variance;

		public Partition(final int blockSize, final int initialBlocks,
				final int testBlocks) {
			this.blockSize = blockSize;
			this.initialBlocks = initialBlocks;
			this.testBlocks = testBlocks;
			mean = MEANS[blockSize];
			variance = VARIANCES[blockSize];
		}
	}
}

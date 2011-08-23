package timezra.nist4j.randomness.tests;

/**
 * The focus of the Overlapping Template Matching test is the number of
 * occurrences of pre-specified target strings. Both this test and the
 * Non-overlapping Template Matching test use an m-bit window to search for a
 * specific m-bit pattern. As with the Non-overlapping Template Matching test,
 * if the pattern is not found, the window slides one bit position. The
 * difference between this test and the Non-overlapping Template Matching test
 * is that when the pattern is found, the window slides only one bit before
 * resuming the search.
 */
public class OverlappingTemplateMatcher extends TemplateMatcher {

	private static final int DEFAULT_TEMPLATE_LENGTH = 9;

	public OverlappingTemplateMatcher() {
		this(DEFAULT_TEMPLATE_LENGTH);
	}

	public OverlappingTemplateMatcher(final int templateLength) {
		this(RandomnessUtils.periodicValues(templateLength), templateLength);
	}

	OverlappingTemplateMatcher(final int periodicTemplate,
			final int templateLength) {
		this(new int[] { periodicTemplate }, templateLength);
	}

	OverlappingTemplateMatcher(final int[] periodicTemplates,
			final int templateLength) {
		super(periodicTemplates, templateLength);
	}

	@Override
	protected int[] calculateBlockPartition(final int n) {
		final int maxNumberOfBlocks = Math.max(2, 2 * n / (3 * templateLength));
		Partition thePartition = new Partition(0, 0, Integer.MAX_VALUE, 0,
				Double.MIN_VALUE);
		for (int numberOfBlocks = 2; numberOfBlocks <= maxNumberOfBlocks; numberOfBlocks++) {
			final int blockSize = n / numberOfBlocks;
			final int lambda = (int) Math.round(lambda(blockSize));
			final Partition theNewPartition = new Partition(blockSize,
					numberOfBlocks, n % numberOfBlocks, lambda, numberOfBlocks
							* RandomnessUtils.probabilityOfOverlappingTemplate(
									2 * lambda, lambda / 2));
			if (theNewPartitionIsBetter(thePartition, theNewPartition)) {
				thePartition = theNewPartition;
			}
		}
		return new int[] { thePartition.blockSize, thePartition.numberOfBlocks };
	}

	private boolean theNewPartitionIsBetter(final Partition old,
			final Partition new_) {
		if (new_.lamdba == 2) {
			if (old.lamdba != 2) {
				return true;
			} else if (old.probability > 5 && new_.probability > 5) {
				if (new_.discards < old.discards) {
					return true;
				}
			} else if (new_.probability > old.probability) {
				return true;
			}
		} else if (old.lamdba != 2) {
			if (old.probability > 5 && new_.probability > 5) {
				if (new_.discards < old.discards) {
					return true;
				}
			} else if (new_.probability > old.probability) {
				return true;
			}
		}
		return false;
	}

	private double lambda(final int blockSize) {
		return (blockSize - templateLength + 1.0) / (1 << templateLength);
	}

	@Override
	protected Matcher createMatcher(final int template, final int blockSize,
			final int numberOfBlocks) {
		return new OverlappingMatcher(template, blockSize, numberOfBlocks);
	}

	private static final class Partition {
		final int blockSize;
		final int numberOfBlocks;
		final int discards;
		final int lamdba;
		final double probability;

		Partition(final int blockSize, final int numberOfBlocks,
				final int discards, final int lamdba, final double probability) {
			this.blockSize = blockSize;
			this.numberOfBlocks = numberOfBlocks;
			this.discards = discards;
			this.lamdba = lamdba;
			this.probability = probability;
		}
	}

	private final class OverlappingMatcher extends SingleMatcher {

		private final int blockSize;

		OverlappingMatcher(final int aperiodicTemplate, final int blockSize,
				final int numberOfBlocks) {
			super(aperiodicTemplate, numberOfBlocks, 0);
			this.blockSize = blockSize;
		}

		@Override
		protected double pValue(final int[] matchesPerWindow) {
			final int K = K(matchesPerWindow);
			final int[] vs = actualOccurrences(K, matchesPerWindow);
			final double[] ps = theoreticalProbabilities(K, eta());
			double chiSquared = 0;
			for (int i = 0; i <= K; i++) {
				final double N_times_p_i = matchesPerWindow.length * ps[i];
				chiSquared += Math.pow(vs[i] - N_times_p_i, 2) / N_times_p_i;
			}
			return RandomnessUtils.igamc(K / 2.0, chiSquared / 2);
		}

		private int K(final int[] matchesPerWindow) {
			int theReal = 0;
			for (int i = 0; i < matchesPerWindow.length; i++) {
				theReal = Math.max(theReal, matchesPerWindow[i]);
			}
			final int theIdeal = (int) Math.round(lambda(blockSize)) * 2;
			return Math.max(1, Math.max(theReal, theIdeal));
		}

		private int[] actualOccurrences(final int K,
				final int[] matchesPerWindow) {
			final int[] vs = new int[K + 1];
			for (int i = 0; i < matchesPerWindow.length; i++) {
				vs[Math.min(K, matchesPerWindow[i])]++;
			}
			return vs;
		}

		private double[] theoreticalProbabilities(final int K, final double eta) {
			final double ps[] = new double[K + 1];
			for (int i = 0; i < ps.length; i++) {
				ps[i] = RandomnessUtils
						.probabilityOfOverlappingTemplate(i, eta);
			}
			return ps;
		}

		private double eta() {
			return lambda(blockSize) / 2;
		}
	}
}

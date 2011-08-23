package timezra.nist4j.randomness.tests;


/**
 * The focus of this test is the number of occurrences of pre-specified target
 * strings. The purpose of this test is to detect generators that produce too
 * many occurrences of a given non-periodic (aperiodic) pattern. For this test
 * and for the Overlapping Template Matching test, an m-bit window is used to
 * search for a specific m-bit pattern. If the pattern is not found, the window
 * slides one bit position. If the pattern is found, the window is reset to the
 * bit after the found pattern, and the search resumes.
 */
public class NonOverlappingTemplateMatcher extends TemplateMatcher {

	private static final int DEFAULT_TEMPLATE_LENGTH = 9;

	public NonOverlappingTemplateMatcher() {
		this(DEFAULT_TEMPLATE_LENGTH);
	}

	public NonOverlappingTemplateMatcher(final int templateLength) {
		this(RandomnessUtils.aperiodicValues(templateLength), templateLength);
	}

	NonOverlappingTemplateMatcher(final int aperiodicTemplate,
			final int templateLength) {
		this(new int[] { aperiodicTemplate }, templateLength);
	}

	NonOverlappingTemplateMatcher(final int[] aperiodicTemplates,
			final int templateLength) {
		super(aperiodicTemplates, templateLength);
	}

	@Override
	protected int[] calculateBlockPartition(final int n) {
		int discards = Integer.MAX_VALUE;
		final int[] MN = new int[2];
		final int maxN = Math.min(n / (templateLength << 1),
				Math.min(100, n - 1));
		for (int N = maxN; N >= 2; N--) {
			final int mod = n % N;
			final int div = n / N;
			if (mod == 0) {
				MN[0] = div;
				MN[1] = N;
				break;
			}
			if (mod < discards) {
				MN[0] = div;
				MN[1] = N;
				discards = mod;
			}
		}
		return MN;
	}

	@Override
	protected Matcher createMatcher(final int template, final int blockSize,
			final int numberOfBlocks) {
		return new NonOverlappingMatcher(template, blockSize, numberOfBlocks);
	}

	private final class NonOverlappingMatcher extends SingleMatcher {
		private final int blockSize;

		NonOverlappingMatcher(final int aperiodicTemplate, final int blockSize,
				final int numberOfBlocks) {
			super(aperiodicTemplate, numberOfBlocks, templateLength - 1);
			this.blockSize = blockSize;
		}

		@Override
		protected double pValue(final int[] matchesPerWindow) {
			final int _2_pow_m = 1 << templateLength;
			final int _2m = templateLength << 1;
			final double mu = (double) (blockSize - templateLength + 1)
					/ _2_pow_m;
			final double sigmaSquared = blockSize * (_2_pow_m - (_2m - 1.0))
					/ (1 << _2m);
			double numerator = 0;
			for (int j = 0; j < matchesPerWindow.length; j++) {
				numerator += Math.pow(matchesPerWindow[j] - mu, 2);
			}
			final double pValue = RandomnessUtils.igamc(
					matchesPerWindow.length / 2.0, numerator
							/ (sigmaSquared * 2));
			return pValue;
		}
	}
}

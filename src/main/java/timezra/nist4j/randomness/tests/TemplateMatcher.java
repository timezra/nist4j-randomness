package timezra.nist4j.randomness.tests;

import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of this test is the number of occurrences of pre-specified target
 * strings. The purpose of this test is to detect generators that produce too
 * many occurrences of a given template pattern. For this test an m-bit window
 * is used to search for a specific m-bit pattern. If the pattern is not found,
 * the window slides one bit position. If the pattern is found, the window is
 * reset and the search resumes.
 */
abstract class TemplateMatcher extends NISTTest {

	final int templates[];
	final int templateLength;

	TemplateMatcher(final int[] templates, final int templateLength) {
		this.templates = templates;
		this.templateLength = templateLength;
	}

	@Override
	public final double pValue(final RandomGenerator e, final int n) {
		final int[] MN = calculateBlockPartition(n);
		final int blockSize = MN[0];
		final int numberOfBlocks = MN[1];
		final Matcher t = new Matchers(templates.length, blockSize,
				numberOfBlocks);
		for (int i = 0; i < numberOfBlocks; i++) {
			int window = 0;
			for (int j = 0; j < blockSize; j++) {
				final int nextBit = e.nextBoolean() ? 1 : 0;
				final int newWindow = window << 1 & (1 << templateLength) - 1;
				window = newWindow | nextBit;
				t.nextWindow(window);
			}
			t.nextBlock();
		}
		return t.pValue();
	}

	protected abstract int[] calculateBlockPartition(final int n);

	protected abstract Matcher createMatcher(final int template,
			final int blockSize, final int numberOfBlocks);

	private final class Matchers implements Matcher {
		private final Matcher[] ts;

		Matchers(final int howMany, final int blockSize,
				final int numberOfBlocks) {
			final Matcher[] ts = new Matcher[howMany];
			for (int i = 0; i < howMany; i++) {
				ts[i] = createMatcher(templates[i], blockSize, numberOfBlocks);
			}
			this.ts = ts;
		}

		public void nextBlock() {
			for (final Matcher t : ts) {
				t.nextBlock();
			}
		}

		public void nextWindow(final int window) {
			for (final Matcher t : ts) {
				t.nextWindow(window);
			}
		}

		public double pValue() {
			double min = Double.MAX_VALUE;
			for (final Matcher t : ts) {
				final double pValue = t.pValue();
				min = Math.min(min, pValue);
			}
			return min;
		}
	}

	abstract class SingleMatcher implements Matcher {
		private final int template;
		private final int turnsLostOnMatch;
		private int turnsLost;
		private int windowIndex = 0;
		private final int[] matchesPerWindow;

		SingleMatcher(final int template, final int numberOfBlocks,
				final int turnsLostOnMatch) {
			this.template = template;
			this.turnsLostOnMatch = turnsLostOnMatch;
			matchesPerWindow = new int[numberOfBlocks];
			turnsLost = templateLength - 1;
		}

		public final void nextBlock() {
			turnsLost = templateLength - 1;
			windowIndex++;
		}

		public final void nextWindow(final int window) {
			if (turnsLost > 0) {
				turnsLost--;
			} else if (template == window) {
				matchesPerWindow[windowIndex]++;
				turnsLost = turnsLostOnMatch;
			}
		}

		public final double pValue() {
			return pValue(matchesPerWindow);
		}

		protected abstract double pValue(final int[] matchesPerWindow);
	}

	static interface Matcher {
		void nextBlock();

		void nextWindow(final int window);

		double pValue();
	}
}

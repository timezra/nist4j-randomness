package timezra.nist4j.randomness.tests;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math.random.RandomGenerator;

/**
 * The focus of this test is the number of cumulatively distinct patterns
 * (words) in the sequence. The purpose of the test is to determine how far the
 * tested sequence can be compressed. The sequence is considered to be
 * non-random if it can be significantly compressed. A random sequence will have
 * a characteristic number of distinct patterns.
 */
public class LempelZivCompression extends NISTTest {

	@Override
	public double pValue(final RandomGenerator e, final int n) {
		final Set<Word> words = new HashSet<Word>();
		NumberStrategy number = new LongStrategy(1, 0);
		for (int i = 0; i < n; i++) {
			number.or(e.nextBoolean());
			final Word w = number.toWord();
			number = words.add(w) ? new LongStrategy(1, 0) : number.timesTwo();
		}
		final double mean = 69586.25;
		final double variance = Math.sqrt(448718.70);
		final double pValue = RandomnessUtils.erfc((mean - words.size())
				/ Math.sqrt(2 * variance)) / 0.5;
		return pValue;
	}

	private static interface NumberStrategy {
		Word toWord();

		void or(boolean bit);

		NumberStrategy timesTwo();
	}

	private static final class BigIntegerStrategy implements NumberStrategy {

		private BigInteger pattern;
		private int length;

		BigIntegerStrategy(final BigInteger initialValue,
				final int initialLength) {
			pattern = initialValue;
			length = initialLength;
		}

		public Word toWord() {
			return new Word(pattern, length);
		}

		public void or(final boolean bit) {
			if (bit) {
				pattern = pattern.or(BigInteger.ONE);
			}
		}

		public NumberStrategy timesTwo() {
			pattern = pattern.shiftLeft(1);
			length++;
			return this;
		}
	}

	private static final class LongStrategy implements NumberStrategy {

		private long pattern;
		private int length;

		LongStrategy(final long initialValue, final int initialLength) {
			pattern = initialValue;
			length = initialLength;
		}

		public Word toWord() {
			return new Word(pattern, length);
		}

		public void or(final boolean bit) {
			pattern |= bit ? 1 : 0;
		}

		public NumberStrategy timesTwo() {
			if (length > 62) {
				final NumberStrategy s = new BigIntegerStrategy(
						BigInteger.valueOf(pattern), length);
				s.timesTwo();
				return s;
			}
			pattern <<= 1;
			length++;
			return this;
		}
	}

	private static final class Word {
		private final Number pattern;
		private final int length;

		Word(final Number pattern, final int length) {
			this.pattern = pattern;
			this.length = length;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + length;
			result = prime * result
					+ (pattern == null ? 0 : pattern.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Word other = (Word) obj;
			if (length != other.length) {
				return false;
			}
			if (pattern == null) {
				if (other.pattern != null) {
					return false;
				}
			} else if (!pattern.equals(other.pattern)) {
				return false;
			}
			return true;
		}
	}
}

package timezra.nist4j.randomness.tests;

import java.util.BitSet;
import java.util.Iterator;

import org.apache.commons.math.random.BitsStreamGenerator;
import org.apache.commons.math.random.RandomGenerator;

public final class RandomGenerators {
	private RandomGenerators() {
		// singleton
	}

	public static RandomGenerator from(final BitSet b) {
		return new BitSetBitsStreamGenerator(b);
	}

	public static RandomGenerator from(final CharSequence s) {
		return new CharSequenceBitsStreamGenerator(s);
	}

	public static RandomGenerator oscillate(final int length, final int period) {
		final BitSet e = new BitSet(length);
		boolean run = true;
		for (int i = 0; i < length; i += period) {
			for (int j = 0; j < period; j++) {
				e.set(i + j, run);
			}
			run = !run;
		}
		return from(e);
	}

	/**
	 * The longest run of ones is determined by the position in the index. The
	 * value at that index is the frequency of the occurrence of that longest
	 * run of ones. For example, i[0] == 1 means there is 1 block where there
	 * are no ones; i[1] == 2 means there are 2 blocks where the longest run of
	 * ones is 2.
	 * 
	 * The block size is inferred from the length of the input. The length of
	 * the Bits String is inferred from the block size and the sum of the
	 * frequencies.
	 */
	public static RandomGenerator distribute(
			final int... longestRunsOfOnesDistribution) {
		final int blocks = RandomnessUtils.sum(longestRunsOfOnesDistribution);
		final int blockSize = longestRunsOfOnesDistribution.length - 1;
		final int length = blocks * blockSize;
		final BitSet e = new BitSet(length);
		int streamPointer = 0;
		for (int i = 0; i < longestRunsOfOnesDistribution.length; i++) {
			for (int j = 0; j < longestRunsOfOnesDistribution[i]; j++) {
				final int start = streamPointer;
				final int end = start + i;
				streamPointer += blockSize;
				e.set(start, end, true);
				e.set(end, streamPointer, false);
			}
		}
		return from(e);
	}

	public static RandomGenerator e(final int length) {
		return e(length, 16);
	}

	static RandomGenerator e(final int length, final int bufferSize) {
		return new EBitsStreamGenerator(length, bufferSize);
	}

	private static final class EBitsStreamGenerator extends
			StaticBitsStreamGenerator {

		private final int bufferSize;

		private final Iterator<Integer> eIterator;
		private int buffer;
		private int bufferCounter;

		EBitsStreamGenerator(final int length, final int bufferSize) {
			this.bufferSize = bufferSize;
			eIterator = RandomnessUtils.eIterator(1 << bufferSize, length);
		}

		@Override
		protected int next(final int bits) {
			if (bits <= bufferCounter) {
				return pop(bits);
			}
			final int difference = bits - bufferCounter;
			final long n = buffer << difference;
			push();
			return (int) (n | pop(difference));
		}

		private int pop(final int bits) {
			bufferCounter -= bits;
			final int toReturn = buffer >> bufferCounter;
			buffer &= (1 << bufferCounter) - 1;
			return toReturn;
		}

		private void push() {
			buffer = eIterator.next();
			bufferCounter = bufferSize;
		}
	}

	private static final class BitSetBitsStreamGenerator extends
			StaticBitsStreamGenerator {

		private int i = 0;
		private final BitSet b;

		BitSetBitsStreamGenerator(final BitSet b) {
			this.b = b;
		}

		@Override
		protected int next(final int bits) {
			int next = 0;
			for (int j = 0; j < bits; j++) {
				next |= b.get(i + j) ? 1 << j : 0;
			}
			i += bits;
			return next;
		}
	}

	private static final class CharSequenceBitsStreamGenerator extends
			StaticBitsStreamGenerator {

		private int i = 0;
		private final CharSequence s;

		CharSequenceBitsStreamGenerator(final CharSequence s) {
			this.s = s;
		}

		@Override
		protected int next(final int bits) {
			return Integer
					.valueOf(s.subSequence(i, i = i + bits).toString(), 2);
		}
	}

	private static abstract class StaticBitsStreamGenerator extends
			BitsStreamGenerator {
		@Override
		public void setSeed(final int seed) {
			// no-op
		}

		@Override
		public void setSeed(final int[] seed) {
			// no-op
		}

		@Override
		public void setSeed(final long seed) {
			// no-op
		}
	}
}

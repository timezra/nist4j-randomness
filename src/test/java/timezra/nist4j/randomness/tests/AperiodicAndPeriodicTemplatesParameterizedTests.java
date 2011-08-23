package timezra.nist4j.randomness.tests;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AperiodicAndPeriodicTemplatesParameterizedTests {

	private final int length;
	private final String[] expectedAperiodicBits;

	@Parameters
	public static List<Object[]> parameters() {
		return asList(new Object[][] { //
				{ 2, new String[] { "01", "10" } },
				{ 3, new String[] { "001", "011", "100", "110" } },
				{
						4,
						new String[] { "0001", "0011", "0111", "1000", "1100",
								"1110" } },
				{
						5,
						new String[] { "00001", "00011", "00101", "01011",
								"00111", "01111", "11100", "11010", "10100",
								"11000", "10000", "11110" } },
				{
						6,
						new String[] { "000001", "000011", "000101", "000111",
								"001011", "001101", "001111", "010011",
								"010111", "011111", "100000", "101000",
								"101100", "110000", "110010", "110100",
								"111000", "111010", "111100", "111110" } },
				{
						7,
						new String[] { "0000001", "0000011", "0000101",
								"0000111", "0001001", "0001011", "0001101",
								"0001111", "0010011", "0010101", "0010111",
								"0011011", "0011101", "0011111", "0100011",
								"0100111", "0101011", "0101111", "0110111",
								"0111111", "1000000", "1001000", "1010000",
								"1010100", "1011000", "1011100", "1100000",
								"1100010", "1100100", "1101000", "1101010",
								"1101100", "1110000", "1110010", "1110100",
								"1110110", "1111000", "1111010", "1111100",
								"1111110" } },
				{
						8,
						new String[] { "00000001", "00000011", "00000101",
								"00000111", "00001001", "00001011", "00001101",
								"00001111", "00010011", "00010101", "00010111",
								"00011001", "00011011", "00011101", "00011111",
								"00100011", "00100101", "00100111", "00101011",
								"00101101", "00101111", "00110101", "00110111",
								"00111011", "00111101", "00111111", "01000011",
								"01000111", "01001011", "01001111", "01010011",
								"01010111", "01011011", "01011111", "01100111",
								"01101111", "01111111", "10000000", "10010000",
								"10011000", "10100000", "10100100", "10101000",
								"10101100", "10110000", "10110100", "10111000",
								"10111100", "11000000", "11000010", "11000100",
								"11001000", "11001010", "11010000", "11010010",
								"11010100", "11011000", "11011010", "11011100",
								"11100000", "11100010", "11100100", "11100110",
								"11101000", "11101010", "11101100", "11110000",
								"11110010", "11110100", "11110110", "11111000",
								"11111010", "11111100", "11111110" } }, });
	}

	public AperiodicAndPeriodicTemplatesParameterizedTests(final int length,
			final String[] expectedBits) {
		this.length = length;
		expectedAperiodicBits = expectedBits;
	}

	@Test
	public void verifyAperiodicValues() throws Exception {
		final int[] aperiodicValues = RandomnessUtils.aperiodicValues(length);
		Arrays.sort(aperiodicValues);
		final int[] expectedInts = toInts(expectedAperiodicBits);
		Arrays.sort(expectedInts);
		assertArrayEquals(expectedInts, aperiodicValues);
	}

	@Test
	public void verifyPeriodicValues() throws Exception {
		final int[] periodicValues = RandomnessUtils.periodicValues(length);
		Arrays.sort(periodicValues);
		final int[] expectedAperiodicValues = toInts(expectedAperiodicBits);
		Arrays.sort(expectedAperiodicValues);
		for (int i = 0, periodicPointer = 0, aperiodicPointer = 0; i < (1 << length) - 1; i++) {
			if (i == periodicValues[periodicPointer]) {
				periodicPointer++;
			} else {
				assertEquals(i, expectedAperiodicValues[aperiodicPointer++]);
			}
		}
		assertEquals((1 << length) - expectedAperiodicValues.length,
				periodicValues.length);
	}

	@Test
	public void verifyAperiodicTemplates() throws Exception {
		final String[] templates = RandomnessUtils.aperiodicTemplates(length);
		Arrays.sort(templates);
		Arrays.sort(expectedAperiodicBits);
		assertArrayEquals(expectedAperiodicBits, templates);
	}

	@Test
	public void verifyPeriodicTemplates() throws Exception {
		final String[] periodicTemplates = RandomnessUtils
				.periodicTemplates(length);
		Arrays.sort(periodicTemplates);
		Arrays.sort(expectedAperiodicBits);
		for (int i = 0, periodicPointer = 0, aperiodicPointer = 0; i < (1 << length) - 1; i++) {
			if (i == Integer.valueOf(periodicTemplates[periodicPointer], 2)
					.intValue()) {
				assertEquals(length,
						periodicTemplates[periodicPointer].length());
				periodicPointer++;
			} else {
				final int expectedAperiodicValue = Integer.valueOf(
						expectedAperiodicBits[aperiodicPointer++], 2)
						.intValue();
				assertEquals(i, expectedAperiodicValue);
			}
		}
		assertEquals((1 << length) - expectedAperiodicBits.length,
				periodicTemplates.length);
	}

	private int[] toInts(final String[] bits) {
		final int[] ints = new int[bits.length];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = Integer.valueOf(bits[i], 2);
		}
		return ints;
	}
}

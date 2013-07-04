package bacci.giovanni.biogiova.sequencing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of the abstract class {@link AlignedSequences}. The sequences
 * in this class must be all of the same length and stored in a
 * {@link LinkedHashMap} with id as key values and sequences as entry values.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class IdsMappedSequences extends AlignedSequences {

	private Map<String, String> sequences = null;
	private SequencesType type = null;
	public int length = 0;
	public int size = 0;

	/**
	 * Standard constructor. If the mapped sequences don't have the same length
	 * an {@link IllegalArgumentException} will be thrown. If one or more
	 * sequence have one or more compound that are not contained in the
	 * {@link SequencesType} alphabet another {@link IllegalArgumentException}
	 * will be thrown.
	 * 
	 * @param sequences
	 *            a map containing id as key and sequences as value
	 * @param type
	 *            the {@link SequencesType}
	 */
	public IdsMappedSequences(Map<String, String> sequences,
			SequencesType type) {
		ArrayList<Integer> lengthList = new ArrayList<>();
		for (String seq : sequences.values()) {
			size++;
			// Checking for the same length
			boolean sameLength = true;
			for (int i : lengthList) {
				sameLength = (i == seq.length());
			}
			if (sameLength) {
				lengthList.add(seq.length());
			} else {
				throw new IllegalArgumentException(
						"Sequences have differet length");
			}
			// Checking for invalid compounds
			for (char c : seq.toLowerCase().toCharArray()) {
				Arrays.sort(type.getAlphabet());
				int result = Arrays.binarySearch(type.getAlphabet(), c);
				if (result < 0) {
					throw new IllegalArgumentException(c
							+ " is not a valid compound");
				}
			}
		}
		this.length = lengthList.get(0);
		this.sequences = sequences;
		this.type = type;
	}

	/**
	 * See {@link AlignedSequences#getSequences()}
	 */
	public char[][] getSequences() {
		char[][] arraySequences = new char[size][length];
		int index = 0;
		for (String seq : sequences.values()) {
			arraySequences[index] = seq.toCharArray();
			index++;
		}
		return arraySequences;
	}

	@Override
	public String[] getIds() {
		String[] ids = new String[sequences.keySet().size()];
		int index = 0;
		for (String id : sequences.keySet()) {
			ids[index] = id;
			index++;
		}
		return ids;
	}

	@Override
	public SequencesType getType() {
		return type;
	}

}

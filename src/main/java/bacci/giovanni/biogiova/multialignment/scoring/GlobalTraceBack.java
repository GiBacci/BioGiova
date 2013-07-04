package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bacci.giovanni.biogiova.AlignmentDirections;
import bacci.giovanni.biogiova.multialignment.MovementTouple;
import bacci.giovanni.biogiova.sequencing.AlignedSequences;
import bacci.giovanni.biogiova.sequencing.FormatUtils;
import bacci.giovanni.biogiova.sequencing.IdsMappedSequences;
import bacci.giovanni.biogiova.sequencing.SequencesType;

/**
 * Implementation of a Global traceBack algorithm. Instances of this class have
 * to be obtained using the factory method
 * {@link ScoreStrategyFactory#getTraceBack()}
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class GlobalTraceBack implements TraceBackAlgorithm {

	AlignedSequences seq1 = null;
	AlignedSequences seq2 = null;

	int numberSeq1 = 0;
	int numberSeq2 = 0;

	/**
	 * Protected Constructor. See {@link ScoreStrategyFactory#getCellBuilder()}
	 */
	protected GlobalTraceBack() {
	}

	/**
	 * This method return an {@link AlignedSequences} instance given an
	 * {@link AlignmentMatrixCell} doubkle array that represent the alignment
	 * matrix of the two sequences seq1 and seq2
	 */
	public List<AlignedSequences> getSequences(AlignmentMatrixCell[][] matrix,
			AlignedSequences seq1, AlignedSequences seq2) {

		this.seq1 = seq1;
		this.seq2 = seq2;

		this.numberSeq1 = seq1.getSequences().length;
		this.numberSeq2 = seq2.getSequences().length;

		SequencesType type1 = seq1.getType();
		SequencesType type2 = seq2.getType();

		int row = seq1.getSequences()[0].length + 1;
		int col = seq2.getSequences()[0].length + 1;

		if ((matrix.length != row) || (matrix[0].length != col)) {
			throw new IllegalArgumentException(
					"Sequences and matrix do not share the same length. Try to exchange seq1 with seq 2.");
		}

		if (type1 != type2) {
			throw new IllegalArgumentException(
					"Sequences are not of the same type");
		}

		boolean finished = false;

		List<AlignedSequences> possibleAlignments = new ArrayList<>();

		while (!finished) {
			finished = true;

			List<char[]> reversedAndTrasposed = new ArrayList<>();
			int i = (row - 1);
			int j = (col - 1);

			AlignmentMatrixCell lastCell = null;
			MovementTouple<AlignmentDirections, Integer> lastTouple = null;

			while (((i > 0) || (j > 0))) {
				AlignmentMatrixCell cell = matrix[i][j];

				if (cell.getMovements().size() > 1) {
					finished = false;
					lastCell = matrix[i][j];
					lastTouple = lastCell.getMovements().get(0);
				}

				AlignmentDirections dir = cell.getMovements().get(0).dir;
				MovementTouple<AlignmentDirections, Integer> touple = cell
						.getMovements().get(0);

				for (int n = 0; n < touple.mov; n++) {

					reversedAndTrasposed.add(go(dir, (i - 1), (j - 1)));

					switch (dir) {
					case DIAGONAL:
						i--;
						j--;
						break;
					case LEFT:
						j--;
						break;
					case UP:
						i--;
						break;
					}
				}
			}

			if (lastCell != null) {
				lastCell.getMovements().remove(lastTouple);
			}

			char[][] alignedSeq = new char[numberSeq1 + numberSeq2][reversedAndTrasposed
					.size()];
			for (int m = (reversedAndTrasposed.size() - 1); m >= 0; m--) {
				char[] c = reversedAndTrasposed.get(m);
				int indexx = (reversedAndTrasposed.size() - 1) - m;
				for (int n = 0; n < c.length; n++) {
					alignedSeq[n][indexx] = c[n];
				}
			}

			String[] ids = new String[seq1.getIds().length
					+ seq2.getIds().length];
			System.arraycopy(seq1.getIds(), 0, ids, 0, seq1.getIds().length);
			System.arraycopy(seq2.getIds(), 0, ids, seq1.getIds().length,
					seq2.getIds().length);

			Map<String, String> sequences = new LinkedHashMap<>();
			for (int m = 0; m < alignedSeq.length; m++) {
				sequences.put(ids[m], String.copyValueOf(alignedSeq[m]));
			}

			AlignedSequences aligned = new IdsMappedSequences(sequences, type1);
			possibleAlignments.add(aligned);

		}

		return possibleAlignments;
	}

	/**
	 * Private method that return an array of aligned character given a
	 * direction and two indexes.
	 * 
	 * @param dir the direction
	 * @param index1 the index of the character in {@link AlignedSequences} one
	 * @param index2 the index of the character in {@link AlignedSequences} two
	 * @return a char array
	 */
	private char[] go(AlignmentDirections dir, int index1, int index2) {

		char[] s1 = null;
		char[] s2 = null;
		char[] both = new char[numberSeq1 + numberSeq2];

		switch (dir) {
		case DIAGONAL:
			s1 = FormatUtils.transpose(seq1.getSequences())[index1];
			s2 = FormatUtils.transpose(seq2.getSequences())[index2];
			System.arraycopy(s1, 0, both, 0, s1.length);
			System.arraycopy(s2, 0, both, s1.length, s2.length);
			break;
		case UP:
			s1 = FormatUtils.transpose(seq1.getSequences())[index1];
			System.arraycopy(s1, 0, both, 0, s1.length);
			for (int i = numberSeq1; i < both.length; i++) {
				both[i] = '-';
			}
			break;
		case LEFT:
			for (int i = 0; i < numberSeq1; i++) {
				both[i] = '-';
			}
			s2 = FormatUtils.transpose(seq2.getSequences())[index2];
			System.arraycopy(s2, 0, both, numberSeq1, s2.length);
			break;
		}
		return both;
	}

}

package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.List;

import bacci.giovanni.biogiova.Aligner;
import bacci.giovanni.biogiova.sequencing.AlignedSequences;

/**
 * Interface that all the Trace back algorithms have to implement.
 * 
 * @see GlobalTraceBack
 * @see ScoreStrategyFactory
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */

public interface TraceBackAlgorithm {

	/**
	 * Method that find all the possible alignments between two
	 * {@link AlignedSequences} given an {@link AlignmentMatrixCell} double
	 * array.
	 * 
	 * @param matix
	 *            see {@link Aligner#align(AlignedSequences, AlignedSequences)}
	 * @param seq1
	 *            the first sequence
	 * @param seq2
	 *            the second sequence
	 * @return a list of {@link AlignedSequences} containing all the possible
	 *         alignments
	 */
	public List<AlignedSequences> getSequences(AlignmentMatrixCell[][] matix,
			AlignedSequences seq1, AlignedSequences seq2);
}

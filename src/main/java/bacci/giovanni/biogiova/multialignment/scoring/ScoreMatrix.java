package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.sequencing.SequencesType;

/**
 * Interface for Score Matrix.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */

public interface ScoreMatrix {

	/**
	 * Method to calculate the score between the two character. Implementations
	 * of this method have to be not case sensitive. If one or both characters
	 * are not present in the alphabet of the matrix this method should throw an
	 * {@link IllegalArgumentException}
	 * 
	 * @param a
	 *            first {@link Character}
	 * @param b
	 *            second {@link Character}
	 * @return the score
	 * @throws IllegalArgumentException
	 *             if at the method has been passed an illegal {@link Character}
	 */
	public double getScore(char a, char b) throws IllegalArgumentException;
	
	/**
	 * This method should return the type of alphabet of the matrix. 
	 * @return the alphabet of the matrix, see {@link SequencesType}
	 */
	public SequencesType getType();
}

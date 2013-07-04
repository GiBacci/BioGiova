package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.Aligner;
import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * Abstract class representing a Scoring Function. 
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public abstract class ScoringFunction {

	protected AlignmentType type = null;
	protected double localLimit = 0.0;
	protected double gop = 0.0;
	protected double gep = 0.0;
	
	/**
	 * Standard constructor. 
	 * @param type {@link AlignmentType}
	 */
	public ScoringFunction(AlignmentType type) {
		this.type = type;
	}

	/**
	 * Each implementation of this class has to implement this function.
	 * 
	 * @param bases
	 *            the bases from which calculate the score in a character array
	 *            form
	 * @return the score attributed to all the bases
	 */
	public abstract double getScore(char[] bases);

	/**
	 * Abstract method. Each implementation of this class has to implement this function.
	 * This function is called every time a score is calculated by an {@link Aligner}.
	 * 
	 * @param score the score calculated 
	 * @return a refined score value
	 */
	public abstract double refineScore(double score);
	
	/**
	 * This method return the {@link AlignmentType} of this {@link ScoringFunction}
	 * @return the alignment type
	 */
	public AlignmentType getType(){
		return type;
	}

	/**
	 * Set the local limit used in the local alignment. 
	 * @param localLimit the local limit
	 */
	public void setLocalLimit(double localLimit) {
		this.localLimit = localLimit;
	}

	/**
	 * Return the local limit used with this function
	 * @return the local limit
	 */ 
	public double getLocalLimit() {
		return localLimit;
	}

	/**
	 * Abstract method that have to return the gap open penalty with the correct sign 
	 * @return the gap open penalty
	 */
	public abstract double getGop();
	
	/**
	 * Abstract method that have to return the gap extension penalty with the correct sign 
	 * @return the gap extension penalty
	 */
	public abstract double getGep();
	
	/**
	 * Abstract method that have to set the {@link ScoreMatrix}
	 * @param matrix the score matrix
	 */
	public abstract void setMatrix(ScoreMatrix matrix);

	/**
	 * Setter method for the gap open penalty
	 * @param gop the gap open penalty
	 */
	public void setGop(double gop) {
		this.gop = gop;
	}

	/**
	 * Setter method for the gap extension penalty
	 * @param gep the gap extension penalty
	 */
	public void setGep(double gep) {
		this.gep = gep;
	}

}

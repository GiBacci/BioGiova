package bacci.giovanni.biogiova.multialignment.scoring;

/**
 * Implementation of {@link GapScoringFunction}.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class SimpleGapscoringFunction extends GapScoringFunction {

	/**
	 * Constructor.
	 * 
	 * @param gop
	 *            the gap open penalty
	 * @param gep
	 *            the gap extension penalty
	 */
	public SimpleGapscoringFunction(double gop, double gep) {
		super(gop, gep);
	}

	/**
	 * This method will return a linear calculation of the gap score depending
	 * on the initial score value and the number of gaps
	 * 
	 * @param initialScore the initial score value
	 * @param movNumber the number of consecutive gaps
	 */
	public double getScore(double initialScore, int movNumber) {
		double score = initialScore + super.getGop();
		for (int i = 1; i < movNumber; i++) {
			score += super.getGep();
		}
		return score;
	}

}

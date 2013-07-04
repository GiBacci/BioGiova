package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * Implementation of the "sum of pair" {@link ScoringFunction}.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class SumOfPairScoringFunction extends ScoringFunction {

	private ScoreMatrix matrix = null;
	
	/**
	 * Constructor.
	 * @param type the alignment type
	 */
	public SumOfPairScoringFunction(AlignmentType type) {
		super(type);
		super.setLocalLimit(0.0);
	}

	/**
	 * This method will calculate the score based on each pairs scores.
	 */
	public double getScore(char[] bases) {
		double score = 0.0;
		for(int i = 0; i < (bases.length - 1); i++){
			for(int m = (i+1); m < bases.length; m++){
				score += matrix.getScore(bases[i], bases[m]);
			}
		}
		return score;
	}

	/**
	 * If the {@link AlignmentType} is a {@link AlignmentType#LOCAL}, then the
	 * given score is compared to the score returned by
	 * {@link ScoringFunction#getLocalLimit()} method. If the score value is
	 * bigger than the limit the score is returned unmodified, otherwise the
	 * {@link ScoringFunction#getLocalLimit()} is returned.
	 */
	public double refineScore(double score) {
		double refinedScore = 0.0;
		if(super.getType() == AlignmentType.LOCAL){
			if(score < super.getLocalLimit()){
				refinedScore = super.getLocalLimit();
			} else {
				refinedScore = score;
			}
		} else {
			refinedScore = score;
		}
		return refinedScore;
	}

	/**
	 * Set the score matrix used by this scoring function
	 * @param matrix the {@link ScoreMatrix}
	 */
	public void setMatrix(ScoreMatrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Return the Gap Open penalty.
	 */
	public double getGop() {
		return -(Math.abs(gop));
	}

	/**
	 * Return the Gap extension Penalty
	 */
	public double getGep() {
		return -(Math.abs(gep));
	}


}

package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.HashMap;

import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * Implementation of {@link ScoringFunction} based on the minimum entropy
 * function. This is a minimization function so value near 0 are the best.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class MinimumEntropyScoringFunction extends ScoringFunction {

	private final char gap = '-';

	/**
	 * See {@link ScoringFunction#ScoringFunction(AlignmentType)}
	 * 
	 * @param type
	 *            the alignment type
	 */
	public MinimumEntropyScoringFunction(AlignmentType type) {
		super(type);
		super.setLocalLimit(10.0);
	}

	@Override
	public double getScore(char[] bases) {
		int n = bases.length;
		HashMap<Character, Integer> map = new HashMap<>();
		for (char c : bases) {
			if (c == gap) {
				n--;
			} else {
				if (map.get(c) == null) {
					map.put(c, 1);
				} else {
					map.put(c, map.get(c) + 1);
				}
			}
		}
		double score = 0.0;
		for (Integer number : map.values()) {
			score += -number * Math.log10((double) number / (double) n);
		}
		return score;
	}

	/**
	 * If the {@link AlignmentType} is a {@link AlignmentType#LOCAL}, then the
	 * given score is compared to the score returned by
	 * {@link ScoringFunction#getLocalLimit()} method. If the score value is
	 * smaller than the limit the score is returned unmodified, otherwise the
	 * {@link ScoringFunction#getLocalLimit()} is returned.
	 */
	public double refineScore(double score) {
		double refinedScore = 0.0;
		if (super.getType() == AlignmentType.LOCAL) {
			if (score > super.getLocalLimit()) {
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
	 * See {@link ScoringFunction#getGop()}
	 */
	public double getGop() {
		return Math.abs(gop);
	}

	/**
	 * See {@link ScoringFunction#getGep()}
	 */
	public double getGep() {
		return Math.abs(gep);
	}

	/**
	 * Empty method! Minimum Entropy implementation of Scoring Function doesn't
	 * need a Score Matrix
	 */
	public void setMatrix(ScoreMatrix matrix) {
		// This scoring function doesn't need a ScoreMatrix!
	}

}

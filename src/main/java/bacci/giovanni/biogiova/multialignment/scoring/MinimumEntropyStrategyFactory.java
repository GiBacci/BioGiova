package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class MinimumEntropyStrategyFactory extends ScoreStrategyFactory {

	private ScoringFunction function = null;
	
	public MinimumEntropyStrategyFactory(AlignmentType type) {
		super(type);
		function = new MinimumEntropyScoringFunction(type);
	}

	public ScoringFunction getScoringFunction() {
		return function;
	}

	public CellBuilder getCellBuilder() {
		CellBuilder builder = new MinimumValueCellBuilder(function);
		return builder;
	}

}

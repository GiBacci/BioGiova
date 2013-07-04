package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * See {@link ScoreStrategyFactory}
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */
public class SumOfPairStrategyFactory extends ScoreStrategyFactory {

	private ScoringFunction function = null;
	
	/**
	 * See {@link ScoreStrategyFactory}
	 * @param type the alignment type
	 */
	public SumOfPairStrategyFactory(AlignmentType type) {
		super(type);
		function = new SumOfPairScoringFunction(type);
	}

	/**
	 * See {@link ScoreStrategyFactory#getScoringFunction()}
	 */
	public ScoringFunction getScoringFunction() {
		return function;
	}

	/**
	 * see {@link ScoreStrategyFactory#getCellBuilder()}
	 */
	public CellBuilder getCellBuilder() {		
		return new MaximumValueCellBuilder(function);
	}


}

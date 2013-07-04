package bacci.giovanni.biogiova.multialignment.scoring;

import bacci.giovanni.biogiova.multialignment.AlignmentType;

/**
 * Abstract Factory. Implementations of this class have to be able to build all the 
 * classes needed for an alignment.
 * 
 * @see ScoringFunction
 * @see CellBuilder
 * @see TraceBackAlgorithm
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */

public abstract class ScoreStrategyFactory {
	
	protected AlignmentType type = null;
	
	/**
	 * Constructor.
	 * @param type the {@link AlignmentType} associated with this class
	 */
	public ScoreStrategyFactory(AlignmentType type) {
		this.type = type;
	}
	
	/**
	 * Factory method used to create instances of {@link ScoringFunction}
	 * @return a {@link ScoringFunction}
	 */
	public abstract ScoringFunction getScoringFunction();
	
	/**
	 * Factory method used to create instances of {@link CellBuilder}
	 * @return a {@link CellBuilder}
	 */
	public abstract CellBuilder getCellBuilder();
	
	/**
	 * This factory method is concrete because the {@link TraceBackAlgorithm} depends
	 * only on the type of the alignment
	 * @return a {@link TraceBackAlgorithm}
	 */
	public TraceBackAlgorithm getTraceBack(){
		TraceBackAlgorithm tb = null;
		if(type == AlignmentType.GLOBAL){
			tb = new GlobalTraceBack();
		}
		if(type == AlignmentType.LOCAL){
			
		}
		return tb;
	}
}

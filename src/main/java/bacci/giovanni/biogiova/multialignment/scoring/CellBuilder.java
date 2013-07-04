package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.LinkedHashMap;
import java.util.Map;

import bacci.giovanni.biogiova.AlignmentDirections;
import bacci.giovanni.biogiova.multialignment.MovementTouple;

/**
 * Factory class for creating instances of {@link AlignmentMatrixCell}. This
 * factory has to be created using another factory method that is
 * {@link ScoreStrategyFactory#getCellBuilder()}
 * 
 * @see AlignmentMatrixCell
 * @see MovementTouple
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public abstract class CellBuilder {
	protected ScoringFunction function = null;
	protected Map<MovementTouple<AlignmentDirections, Integer>, Double> values = null;

	/**
	 * Constructor that create an instance of this class given a
	 * {@link ScoringFunction}.
	 * 
	 * @param function
	 *            the {@link ScoringFunction} associated to this class.
	 */
	protected CellBuilder(ScoringFunction function) {
		this.function = function;
		this.values = new LinkedHashMap<MovementTouple<AlignmentDirections, Integer>, Double>();
	}

	/**
	 * Abstract method that return an {@link AlignmentMatrixCell}. This is the
	 * factory method of this factory. This factory has to be created using
	 * anothor factory method that is
	 * {@link ScoreStrategyFactory#getCellBuilder()}
	 * 
	 * @return an {@link AlignmentMatrixCell} based on the values previously set
	 */
	abstract public AlignmentMatrixCell getMatrixCell();

	/**
	 * This method set a {@link MovementTouple} value and a score associated to
	 * that tuple and to this cell.
	 * 
	 * @param touple
	 *            the {@link MovementTouple}
	 * @param score
	 *            a score
	 */
	public void setValue(MovementTouple<AlignmentDirections, Integer> touple,
			Double score) {
		values.put(touple, score);
	}

	/**
	 * Useful method to clear the builder from previous values.
	 */
	public void reset() {
		values.clear();
	}

}

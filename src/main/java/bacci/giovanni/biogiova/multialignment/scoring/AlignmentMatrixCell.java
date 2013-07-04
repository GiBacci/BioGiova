package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.List;

import bacci.giovanni.biogiova.AlignmentDirections;
import bacci.giovanni.biogiova.multialignment.MovementTouple;

/**
 * Concrete class that represent a cell of an alignment matrix. Instances of
 * this class have to be created using the factory method
 * {@link CellBuilder#getMatrixCell()}.
 * 
 * @see CellBuilder
 * @see MaximumValueCellBuilder
 * @see MinimumValueCellBuilder
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */

public class AlignmentMatrixCell {
	private List<MovementTouple<AlignmentDirections, Integer>> movements = null;
	private double score = 0.0;

	/**
	 * Protected constructor. Instances of this class have to be created only
	 * using the factory method {@link CellBuilder#getMatrixCell()}
	 * 
	 * @param movements
	 * @param score
	 */
	protected AlignmentMatrixCell(
			List<MovementTouple<AlignmentDirections, Integer>> movements,
			double score) {
		this.movements = movements;
		this.score = score;
	}

	/**
	 * Method to get the score of this cell.
	 * 
	 * @return the score of this cell
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Method to get a list of the allowed movements of this cell as a
	 * {@link List} of {@link MovementTouple}.
	 * 
	 * @return a list of {@link MovementTouple}
	 */
	public List<MovementTouple<AlignmentDirections, Integer>> getMovements() {
		return movements;
	}

}

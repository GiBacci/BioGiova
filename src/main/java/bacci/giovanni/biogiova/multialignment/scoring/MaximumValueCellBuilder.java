package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import bacci.giovanni.biogiova.AlignmentDirections;
import bacci.giovanni.biogiova.multialignment.MovementTouple;

/**
 * Implementation of {@link CellBuilder} that return a cell with the highest
 * score given.
 * 
 * @see CellBuilder
 * @see MinimumValueCellBuilder
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */
public class MaximumValueCellBuilder extends CellBuilder {

	protected MaximumValueCellBuilder(ScoringFunction function) {
		super(function);
	}

	/**
	 * Implementation of the abstract method {@link CellBuilder#getMatrixCell()}
	 * . This method will return the cell with the highest score value and the
	 * respective direction
	 */
	public AlignmentMatrixCell getMatrixCell() {
		AlignmentMatrixCell cell = null;
		List<MovementTouple<AlignmentDirections, Integer>> movements = new ArrayList<>();
		double max = Collections.max(super.values.values());
		for (Entry<MovementTouple<AlignmentDirections, Integer>, Double> entry : super.values
				.entrySet()) {
			if (entry.getValue() == max) {
				movements.add(entry.getKey());
			}
		}

		cell = new AlignmentMatrixCell(movements,
				super.function.refineScore(max));
		super.reset();
		return cell;
	}

}

package bacci.giovanni.biogiova.multialignment.scoring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import bacci.giovanni.biogiova.AlignmentDirections;
import bacci.giovanni.biogiova.multialignment.MovementTouple;

public class MinimumValueCellBuilder extends CellBuilder {

	protected MinimumValueCellBuilder(ScoringFunction function) {
		super(function);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AlignmentMatrixCell getMatrixCell() {
		AlignmentMatrixCell cell = null;
		List<MovementTouple<AlignmentDirections, Integer>> movements = new ArrayList<>();
		double min = Collections.min(super.values.values());
		for (Entry<MovementTouple<AlignmentDirections, Integer>, Double> entry : super.values.entrySet()) {
			if (entry.getValue() == min) {
				movements.add(entry.getKey());
			}
		}
		
		cell = new AlignmentMatrixCell(movements,
				super.function.refineScore(min));
		super.reset();
		return cell;
	}

}

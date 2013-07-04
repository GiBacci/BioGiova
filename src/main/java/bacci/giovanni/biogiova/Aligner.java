package bacci.giovanni.biogiova;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bacci.giovanni.biogiova.multialignment.AlignmentType;
import bacci.giovanni.biogiova.multialignment.MovementTouple;
import bacci.giovanni.biogiova.multialignment.matrices.ScoreMatricesType;
import bacci.giovanni.biogiova.multialignment.scoring.AlignmentMatrixCell;
import bacci.giovanni.biogiova.multialignment.scoring.CellBuilder;
import bacci.giovanni.biogiova.multialignment.scoring.GapScoringFunction;
import bacci.giovanni.biogiova.multialignment.scoring.ScoreMatrix;
import bacci.giovanni.biogiova.multialignment.scoring.ScoreMatrixFactory;
import bacci.giovanni.biogiova.multialignment.scoring.ScoreStrategyFactory;
import bacci.giovanni.biogiova.multialignment.scoring.ScoringFunction;
import bacci.giovanni.biogiova.multialignment.scoring.SimpleGapscoringFunction;
import bacci.giovanni.biogiova.multialignment.scoring.SumOfPairStrategyFactory;
import bacci.giovanni.biogiova.multialignment.scoring.TraceBackAlgorithm;
import bacci.giovanni.biogiova.sequencing.AlignedSequences;
import bacci.giovanni.biogiova.sequencing.FormatUtils;

public class Aligner {

	private ScoreMatrix matrix = ScoreMatrixFactory
			.newInstance(ScoreMatricesType.IDENTITY);

	private ScoringFunction function = null;
	private GapScoringFunction gapFunction = null;
	private CellBuilder builder = null;

	private TraceBackAlgorithm tb = null;
	private ScoreStrategyFactory ssf = null;
	private double gop = 0.0;
	private double gep = 0.0;

	public Aligner(AlignmentType type) {
		ssf = new SumOfPairStrategyFactory(type);
	}

	public List<AlignedSequences> align(AlignedSequences seq1,
			AlignedSequences seq2) {

		function = ssf.getScoringFunction();
		function.setMatrix(matrix);
		function.setGep(gep);
		function.setGop(gop);
		gapFunction = new SimpleGapscoringFunction(function.getGop(), function.getGep());
		tb = ssf.getTraceBack();
		builder = ssf.getCellBuilder();

		MovementTouple<AlignmentDirections, Integer> diagonalTouple = null;
		MovementTouple<AlignmentDirections, Integer> upTouple = null;
		MovementTouple<AlignmentDirections, Integer> leftTouple = null;

		int col = seq2.getSequences()[0].length + 1;
		int row = seq1.getSequences()[0].length + 1;
		
		AlignmentMatrixCell[][] alignmentMatrix = new AlignmentMatrixCell[row][col];

		diagonalTouple = new MovementTouple<AlignmentDirections, Integer>(
				AlignmentDirections.DIAGONAL, 1);
		builder.setValue(diagonalTouple, 0.0);
		alignmentMatrix[0][0] = builder.getMatrixCell();

		leftTouple = new MovementTouple<AlignmentDirections, Integer>(
				AlignmentDirections.LEFT, 1);
		builder.setValue(leftTouple, function.getGop());
		alignmentMatrix[0][1] = builder.getMatrixCell();

		upTouple = new MovementTouple<AlignmentDirections, Integer>(
				AlignmentDirections.UP, 1);
		builder.setValue(upTouple, function.getGop());
		alignmentMatrix[1][0] = builder.getMatrixCell();

		for (int i = 2; i < col; i++) {
			double value = alignmentMatrix[0][i - 1].getScore();
			leftTouple = new MovementTouple<AlignmentDirections, Integer>(
					AlignmentDirections.LEFT, 1);
			builder.setValue(leftTouple, value + function.getGep());
			alignmentMatrix[0][i] = builder.getMatrixCell();
		}

		for (int i = 2; i < row; i++) {
			double value = alignmentMatrix[i - 1][0].getScore();
			upTouple = new MovementTouple<AlignmentDirections, Integer>(
					AlignmentDirections.UP, 1);
			builder.setValue(upTouple, value + function.getGep());
			alignmentMatrix[i][0] = builder.getMatrixCell();
		}

		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				char[] a = FormatUtils.transpose(seq1.getSequences())[i - 1];
				char[] b = FormatUtils.transpose(seq2.getSequences())[j - 1];
				char[] both = new char[a.length + b.length];
				System.arraycopy(a, 0, both, 0, a.length);
				System.arraycopy(b, 0, both, a.length, b.length);

				double upPenalty = function.getGop();
				double leftPenalty = function.getGop();

				Map<Double, Integer> upPenaltyList = new LinkedHashMap<>();
				for (int r = 0; r < i; r++) {
					AlignmentMatrixCell cell = alignmentMatrix[r][j];
					int movement = i - r;
					upPenalty = gapFunction.getScore(cell.getScore(), movement);
					upPenaltyList.put(upPenalty, (i - r));
				}

				Map<Double, Integer> leftPenaltyList = new LinkedHashMap<>();
				for (int r = 0; r < j; r++) {
					AlignmentMatrixCell cell = alignmentMatrix[i][r];
					int movement = j - r;
					leftPenalty = gapFunction.getScore(cell.getScore(), movement);
					leftPenaltyList.put(leftPenalty, (j - r));
				}

				double diagonal = alignmentMatrix[i - 1][j - 1].getScore()
						+ function.getScore(both);
				diagonalTouple = new MovementTouple<AlignmentDirections, Integer>(
						AlignmentDirections.DIAGONAL, 1);

				double up = Collections.max(upPenaltyList.keySet());
				int upMov = upPenaltyList.get(up);
				upTouple = new MovementTouple<AlignmentDirections, Integer>(
						AlignmentDirections.UP, upMov);
				
				double left = Collections.max(leftPenaltyList.keySet());
				int leftMov = leftPenaltyList.get(left);
				leftTouple = new MovementTouple<AlignmentDirections, Integer>(
						AlignmentDirections.LEFT, leftMov);

				builder.setValue(diagonalTouple, diagonal);
				builder.setValue(upTouple, up);
				builder.setValue(leftTouple, left);

				alignmentMatrix[i][j] = builder.getMatrixCell();

			}
		}

//		for (AlignmentMatrixCell[] m : alignmentMatrix) {
//			for (AlignmentMatrixCell mm : m) {
//				System.out.print(mm.getScore() + "  ");
//			}
//			System.out.println();
//		}

		// for (AlignmentMatrixCell[] m : alignmentMatrix) {
		// for (AlignmentMatrixCell mm : m) {
		// System.out.print(mm.getDirections().size());
		// for (AlignmentDirections a : mm.getDirections()) {
		// System.out.print(a + " | ");
		// }
		// }
		// System.out.println();
		// }

//		for (AlignmentMatrixCell[] m : alignmentMatrix) {
//			for (AlignmentMatrixCell mm : m) {
//				System.out.print(mm.getMovements().size());
//				for (MovementTouple<AlignmentDirections, Integer> a : mm
//						.getMovements()) {
////					System.out.print(a.dir + ":" + a.mov + " | ");
//					System.out.printf("%8s:%s | ", a.dir, a.mov);
//				}
//			}
//			System.out.println();
//		}
		 return tb.getSequences(alignmentMatrix, seq1, seq2);
	}

	/**
	 * Set the gap open penalty for this aligner. 
	 * @param gop the gap open penalty without any sign
	 */
	public void setGop(double gop) {
		this.gop = gop;
	}

	/**
	 * set the gap extension penalty for this aligner
	 * @param gep the gap open penalty without any sign
	 */
	public void setGep(double gep) {
		this.gep = gep;
	}

	/**
	 * Set the Score matrix for this aligner
	 * @param matrix the {@link ScoreMatrix}
	 */
	public void setMatrix(ScoreMatrix matrix) {
		this.matrix = matrix;
	}

}

package bacci.giovanni.biogiova.multialignment.scoring;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import bacci.giovanni.biogiova.sequencing.SequencesType;

/**
 * Implementation of {@link ScoreMatrix} interface. This class load a matrix
 * from one of the file in "matrices" directory into an {@link HashMap}. This
 * class has to be instantiated using the
 * {@link ScoreMatrixFactory#newInstance(bacci.giovanni.biogiova.multialignment.matrices.ScoreMatricesType)}
 * factory method.
 * 
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni
 *         Bacci</a>
 * 
 */

public class LoadedScoreMatrix implements ScoreMatrix {

	private HashMap<Character, HashMap<Character, Double>> matrix = null;
	private InputStream in = null;
	private SequencesType type = null;
	private final String separator = ";";

	/**
	 * Constructor protected.
	 * 
	 * @param in
	 *            the {@link InputStream} pointing at the matrix file
	 */
	protected LoadedScoreMatrix(InputStream in, SequencesType type) {
		this.in = in;
		this.matrix = new HashMap<>();
		this.type = type;
		loadMatrix();
	}

	/**
	 * Public constructor for custom matrix. Each custom matrix has to be in the
	 * correct format:
	 * 
	 * <pre>
	 *           ;Compound1;Compound2;Compound3;Compound4
	 *  Compound1; score1-1; score1-2; score1-3; score1-4
	 *  Compound2; score2-1; score2-2; score2-3; score2-4
	 *  Compound3; score3-1; score3-2; score3-3; score3-4
	 *  Compound4; score4-1; score4-2; score4-3; score4-4
	 * </pre>
	 * 
	 * One can skip the white spaces but has to maintain the correct separation.
	 * Each matrix has specify all the character in the
	 * {@link SequencesType#getAlphabet()} array otherwise an {@link IllegalArgumentException}
	 * will be thrown.
	 * 
	 * @param matrix
	 *            the custom matrix file
	 * @throws FileNotFoundException
	 *             if the file doesn't exist
	 * @throws IllegalArgumentException
	 *             if the given matrix don't respect the given
	 *             {@link SequencesType}
	 */
	public LoadedScoreMatrix(File matrix, SequencesType type)
			throws FileNotFoundException {
		in = new FileInputStream(matrix);
		this.matrix = new HashMap<>();
		this.type = type;
		loadMatrix();
	}

	/**
	 * Private method to load a matrix from a file. For the matrix format see
	 * {@link LoadedScoreMatrix#LoadedScoreMatrix(File)} constructor.
	 */
	private void loadMatrix() {
		ArrayList<Character> alphabet = new ArrayList<>();
		ArrayList<Character> columnChar = new ArrayList<>();
		ArrayList<Character> rowChar = new ArrayList<>();

		for (char c : type.getAlphabet()) {
			alphabet.add(Character.toUpperCase(c));
		}
		Collections.sort(alphabet);

		Scanner scan = new Scanner(in);
		String[] column = scan.nextLine().split(separator);
		while (scan.hasNext()) {
			HashMap<Character, Double> line = new HashMap<>();
			String[] row = scan.nextLine().split(separator);
			char c = Character.toUpperCase(row[0].trim().charAt(0));
			rowChar.add(c);
			for (int i = 1; i < row.length; i++) {
				char cc = Character.toUpperCase(column[i].trim().charAt(0));
				if (!columnChar.contains(cc)) {
					columnChar.add(cc);
				}
				Double d = new Double(row[i]);
				line.put(cc, d);
			}
			matrix.put(c, line);
		}
		Collections.sort(rowChar);
		Collections.sort(columnChar);

		if ((!alphabet.equals(columnChar)) || (!alphabet.equals(rowChar))) {
			scan.close();
			throw new IllegalArgumentException(
					"Specified matrixhas not all the character of the alphabet");
		}

		scan.close();
	}

	/**
	 * See {@link ScoreMatrix#getScore(char, char)}
	 */
	@Override
	public double getScore(char a, char b) {
		return matrix.get(Character.toUpperCase(a)).get(
				Character.toUpperCase(b));
	}

	/**
	 * See {@link ScoreMatrix#getType()}
	 */
	@Override
	public SequencesType getType() {
		return type;
	}

}

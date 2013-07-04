package bacci.giovanni.biogiova.alignment;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Classe che rappresenta un lista di matrici implementate nella cartella
 * "Matrices"
 * 
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public enum Matrices {

	IUB(AlphabetType.DNA), WEIGHT(AlphabetType.DNA), BLAST(AlphabetType.DNA), IDENTITY(
			AlphabetType.DNA);
	
	private String file = "matricesfiles/";
	private InputStream in = null;
	private Alphabet alph = null;

	/**
	 * Ogni enum ha un suo alfabeto a seconda della matrice
	 * 
	 * @param type
	 *            l'alfabeto proprio della matrice di score
	 */
	Matrices(AlphabetType type) {
		switch (this.toString()) {
		case "IUB":
			in = this.getClass().getResourceAsStream(file + "iub.txt");
			break;
		case "WEIGHT":
			in = this.getClass().getResourceAsStream(file + "weight.txt");
			break;
		case "BLAST":
			in = this.getClass().getResourceAsStream(file + "blast.txt");
			break;
		case "IDENTITY":
			in = this.getClass().getResourceAsStream(file + "identity.txt");
			break;
		}
		this.alph = new Alphabet(type);
	}

	/**
	 * Rende l'alfabeto proprio della matrice scelta
	 * 
	 * @return l'alfabeto
	 */
	public Alphabet getAlphabet() {
		return this.alph;
	}

	/**
	 * Rende la matrice come un dizionario di dizionari
	 * 
	 * @return la matrice
	 */
	public Map<Character, Map<Character, Double>> getMatrix() {
		Map<Character, Map<Character, Double>> substIndex = new HashMap<>();
		Scanner scan = new Scanner(in);
		String[] column = scan.nextLine().toUpperCase().split(";");
		while (scan.hasNext()) {
			Map<Character, Double> line = new HashMap<>();
			String[] row = scan.nextLine().toUpperCase().split(";");
			char c = row[0].trim().charAt(0);
			for (int i = 1; i < row.length; i++) {
				char cc = column[i].trim().charAt(0);
				Double d = new Double(row[i]);
				line.put(cc, d);
			}
			substIndex.put(c, line);
		}
		return substIndex;
	}
}

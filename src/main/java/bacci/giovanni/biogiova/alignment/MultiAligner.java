/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bacci.giovanni.biogiova.alignment;

import java.util.Iterator;

/**
 *
 * @author giovanni
 */
public class MultiAligner {

    private Sequences sequences = null;
    private ScoreMatrix matrix = null;
    private double[][] scoreMatrix = null;
    private double[][] identityMatrix = null;
    private int size = 0;

    public MultiAligner(ScoreMatrix matrix, Sequences sequences) {
        this.sequences = sequences;
        this.size = sequences.size();
        this.matrix = matrix;
    }

    public void getMatrix() throws InvalidAlphabetException, 
            DifferentLengthException {
        int dim = this.size - 1;
        this.scoreMatrix = new double[dim][dim];
        this.identityMatrix = new double[dim][dim];
        Sequences seq1 = this.sequences;
        Sequences seq2 = this.sequences;
        
        int index = 0;
        for (int i = 0; i < this.size; i++) {
            Sequence s1 = seq1.get(i);
            index++;
            for (int j = index; j < this.size; j++) {
                Sequence s2 = seq2.get(j);
                Aligner align = new Aligner(this.matrix, s1, s2);
                align.globalAlignment();
                double score = align.getScore();
                double identity = align.getIdentityScore();
                this.scoreMatrix[(j - 1)][i] = score;
                this.identityMatrix[(j - 1)][i] = identity;
            }
        }
        
        for (int i = 0; i < (this.size - 1); i++) {
            for (int j = 0; j < (this.size - 1); j++) {
                System.out.print(
                        String.format("%+8.1f", this.scoreMatrix[i][j]));
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < (this.size - 1); i++) {
            for (int j = 0; j < (this.size - 1); j++) {
                System.out.print(
                        String.format("%+8.1f", this.identityMatrix[i][j]));
            }
            System.out.println();
        }
    }
}

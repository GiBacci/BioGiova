/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bacci.giovanni.biogiova.alignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Classe che rappresenta un gruppo di sequenze allineate.
 *
 * @author giovanni
 */
public class AlignedSequences {

    private List<Sequence> alignedSequences = null;
    private Alphabet alphabet = null;
    // la lunghezza delle sequenze allineate
    private int length = 0;
    // la quantità di sequenze allineate
    private int size = 0;
    private ScoreMatrix scoreMatrix = null;
    private List<Map<Character, Integer>> baseOccurrence = new ArrayList<>();

    /**
     * Costruisce un istanza della classe {@link AlignedSequences} partendo da
     * una lista di sequenze allineate.<p> E' necessario che le sequenze
     * presenti nella lista siano:<br> 1 - Tutte con lo stesso
     * {@link Alphabet}<br> 2 - Tutte della stessa lunghezza (inclusi i gap)<br>
     * 3 - La {@link ScoreMatrix} sia la stessa con la quale le sequenze sono
     * state allineate<br>
     *
     * @param alphabet {@link Alphabet} delle sequenze nella lista
     * @param alignedSequences lista di sequenze della stessa lunghezza
     * pre-allineate
     * @param scoreMatrix {@link ScoreMatrix} con la quale le sequenze presenti
     * nella lista sono state precedentemente allineate
     * @throws InvalidAlphabetException Se l'alfabeto non è lo stesso per tutte
     * le sequenze
     * @throws DifferentLengthException Se le sequenze non hanno la stessa
     * lunghezza
     */
    public AlignedSequences(Alphabet alphabet, List<Sequence> alignedSequences,
            ScoreMatrix scoreMatrix)
            throws InvalidAlphabetException, DifferentLengthException {
        boolean sameLength = true;
        boolean sameAlph = true;
        int length = alignedSequences.get(0).length();
        for (Sequence s : alignedSequences) {
            if (!s.getAlphabet().equals(alphabet)) {
                throw new InvalidAlphabetException(
                        "Sequences doesn't have the same Alphabet.");
            } else {
                if (s.length() != length) {
                    throw new DifferentLengthException(
                            "Sequences doesn't have the same length.");
                }
            }
        }
        this.alphabet = alphabet;
        this.alignedSequences = alignedSequences;
        this.size = alignedSequences.size();
        this.length = length;
        this.scoreMatrix = scoreMatrix;
        char[] alph = this.alphabet.alphabet;
        for (int i = 0; i < this.length; i++) {
            Map<Character, Integer> occurrence = new HashMap<>();
            this.baseOccurrence.add(occurrence);
        }
        for (int i = 0; i < this.size; i++) {
            Sequence s = this.alignedSequences.get(i);
            for (int m = 0; m < s.length(); m++) {
                if (this.baseOccurrence.get(m).keySet().isEmpty()) {
                    for (Character c : alph) {
                        this.baseOccurrence.get(m).put(c, 0);
                    }
                }
                char c = s.getSequence()[m];
                int f = this.baseOccurrence.get(m).get(c);
                this.baseOccurrence.get(m).put(c, (f + 1));
            }
        }
    }

    protected List<Map<Character, Integer>> getBaseDistribution() {
        return this.baseOccurrence;
    }

    /**
     * Calcala il valore di score della posizione inserita come somma di tutte
     * le coppie di basi in quella posizione. La posizione è riderita alla
     * posizione nella sequenza di DNA e non all'index (va da 1 alla lunghezza
     * dell'allineamento)
     *
     * @param position posizione index = 0 -> position = 1
     * @return lo score associato alla somma delle coppie in quella posizione
     */
    protected double sumOfPairScore(int position) {
        double score = 0.0;
        List<Character> column = new ArrayList<>();
        for (Sequence s : this.alignedSequences) {
            column.add(s.getSequence()[(position - 1)]);
        }
        for (int i = 0; i < column.size() - 1; i++) {
            char c1 = column.get(i);
            for (int n = i + 1; n < column.size(); n++) {
                char c2 = column.get(n);
                try {
                    score += this.scoreMatrix.getScore(c1, c2);
                } catch (InvalidAlphabetException ainvAl) {
                }
            }
        }
        return score;
    }

    /**
     * Calcola lo score di tutto l'allineamento come somma degli score di tutte
     * le coppie.
     *
     * @return lo score di tutto l'allineamento
     */
    protected double sumOfPairScore() {
        double score = 0.0;
        for (int b = 0; b < this.length; b++) {
            List<Character> column = new ArrayList<>();
            for (Sequence s : this.alignedSequences) {
                column.add(s.getSequence()[b]);
            }
            for (int i = 0; i < column.size() - 1; i++) {
                char c1 = column.get(i);
                for (int n = i + 1; n < column.size(); n++) {
                    char c2 = column.get(n);
                    try {
                        score += this.scoreMatrix.getScore(c1, c2);
                    } catch (InvalidAlphabetException ainvAl) {
                    }
                }
            }
        }
        return score;
    }

    /**
     * Rende il valore di score come somma delle coppie attribuito alla
     * posizione indicata più la base che stiamo aggiungendo come un carattere
     *
     * @param c la base da aggiungere (in maiuscolo)
     * @param position la posizione in cui aggiungere la base e calcolare
     * @return lo score associato alla posizione indicata come se questa
     * contenesse anche la base c
     */
    protected double sumOfPairScore(char c, int position) {
        double score = 0.0;
        List<Character> column = new ArrayList<>();
        column.add(c);
        for (Sequence s : this.alignedSequences) {
            column.add(s.getSequence()[(position - 1)]);
        }
        for (int i = 0; i < column.size() - 1; i++) {
            char c1 = column.get(i);
            for (int n = i + 1; n < column.size(); n++) {
                char c2 = column.get(n);
                try {
                    score += this.scoreMatrix.getScore(c1, c2);
                } catch (InvalidAlphabetException ainvAl) {
                }
            }
        }
        return score;
    }

    /**
     * Calcola il valore di score come minima entropia della posizione
     * desiderata. La posizione è riderita alla posizione nella sequenza di DNA
     * e non all'index (va da 1 alla lunghezza dell'allineamento)
     *
     * @param position posizione index = 0 -> position = 1
     * @return lo score
     */
    protected double minimumEntropyScore(int position) {
        double score = 0.0;
        double freq = (double) this.size;
        Map<Character, Integer> map = this.baseOccurrence.get(position - 1);
        for (Character c : map.keySet()) {
            double num = (double) map.get(c);
            if (num > 0.0) {
                score += num * Math.log10(num / freq);
            } else {
                continue;
            }
        }
        if (score < 0.0) {
            score = -score;
        }
        return score;
    }

    /**
     * Calcola il valore di score di minima entropia per tutto l'allineamento
     *
     * @return il valore di score
     */
    protected double minimumEntropyScore() {
        double score = 0.0;
        double freq = (double) this.size;
        for (Map<Character, Integer> map : this.baseOccurrence) {
            for (Character c : map.keySet()) {
                double num = (double) map.get(c);
                if (num > 0.0) {
                    score += num * Math.log10(num / freq);
                } else {
                    continue;
                }
            }
        }
        if (score < 0.0) {
            score = -score;
        }
        return score;
    }

    /**
     * Calcola il valore di score di minima entropia della posizione desiderata
     * inserendo una base nell'allineamento
     * @param c la base da inserire (maiuscola)
     * @param position la posizione
     * @return lo score
     */
    protected double minimumEntropyScore(char c, int position) {
        double score = 0.0;
        double freq = (double) this.size + 1;
        Map<Character, Integer> map = this.baseOccurrence.get(position - 1);
        map.put(c, map.get(c) + 1);
        for (Character cr : map.keySet()) {
            double num = (double) map.get(cr);
            if (num > 0.0) {
                score += num * Math.log10(num / freq);
            } else {
                continue;
            }
        }
        if (score < 0.0) {
            score = -score;
        }
        return score;
    }
    
    /**
     * Rende la lunghezza dell'allineamento in termini di basi
     * @return 
     */
    public int getLength(){
        return this.length;
    }
    
    /**
     * Rende la grandezza dell'allineamneto in termini di sequenze
     * @return 
     */
    public int getSize(){
        return this.size;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Sequence s : this.alignedSequences) {
            builder.append(s + System.lineSeparator());
        }
        return builder.toString();
    }
}

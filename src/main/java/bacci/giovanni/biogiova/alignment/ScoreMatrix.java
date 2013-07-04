package bacci.giovanni.biogiova.alignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe che rappresenta una matrice di scoring dato un determinato alfabeto
 *
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class ScoreMatrix {

    private Map<Character, Map<Character, Double>> scoreMap = new HashMap<>();
    private Alphabet alph;
    private double gop = 0.0;
    private double gep = 0.0;

    /**
     * Crea una matrice di scoring dato un determinato alfabeto. La matrice
     * creata in questo modo è vuota.
     *
     * @param alph l'alfabeto
     */
    public ScoreMatrix(Alphabet alph) {
        this.alph = alph;
    }
    
    /**
     * Crea una matrice di score a partire da una matrice implementata. 
     * L'alfabeto è quello specifico della matrice scelta.
     * @param mat la matrice
     */
    public ScoreMatrix(Matrices mat){
        this.alph = mat.getAlphabet();
        this.scoreMap = mat.getMatrix();
    }

    /**
     * Setta gli score della matrice, viene attribuito solamente un valore di
     * match ed uno di mismatch
     *
     * @param matches score da attribuire ai match
     * @param mismatches score da attribuire ai mismatch
     */
    public void setScore(double matches, double mismatches) {
        int length = this.alph.alphabet.length;
        for (int i = 0; i < length; i++) {
            Map<Character, Double> line = new HashMap<>();
            for (int j = 0; j < length; j++) {
                if (this.alph.alphabet[i] == this.alph.alphabet[j]) {
                    line.put(this.alph.alphabet[j], matches);
                } else {
                    line.put(this.alph.alphabet[j], mismatches);
                }
            }
            this.scoreMap.put(this.alph.alphabet[i], line);
        }
    }

    /**
     * Assegna un valore di score ad una certa coppia (viene assegnato lo stesso
     * valore per il reciproco)
     *
     * @param c1 Primo carattere della coppia
     * @param c2 Secondo carattere della coppia
     * @param score Punteggio da attribuire
     * @throws InvalidAlphabetException Se i caratteri non sono presenti nell'{@link Alphabet}
     */
    public void setScore(char c1, char c2, double score)
            throws InvalidAlphabetException {

        c1 = Character.toUpperCase(c1);
        c2 = Character.toUpperCase(c2);

        try {
            Map<Character, Double> line = this.scoreMap.get(c1);
            line.put(c2, score);
            this.scoreMap.put(c1, line);
        } catch (NullPointerException nullExc) {
            throw new InvalidAlphabetException(
                    "Character/s are not present in the alphabet");
        }
        
        try {
            Map<Character, Double> line = this.scoreMap.get(c2);
            line.put(c1, score);
            this.scoreMap.put(c2, line);
        } catch (NullPointerException nullExc) {
            throw new InvalidAlphabetException(
                    "Character/s are not present in the alphabet");
        }
    }
    
    /**
     * Fissa la penalità per l'apertura di un gap 
     * (è una penalità quindi sarà sempre negativa)
     * @param gop la penalità
     */
    public void setGapOpenPenalty(double gop){
        this.gop = -(Math.abs(gop));
    }
    
    /**
     * Fissa la penalità per l'estensione di un gap 
     * (è una penalità quindi sarà sempre negativa)
     * @param gep la penalità
     */
    public void setGapExtensionPenalty(double gep){
        this.gep = -(Math.abs(gep));
    }

    /**
     * Rende gli score della matrice costruita come un HashMap
     *
     * @return un HashMap bidimensionale contenente gli scores
     */
    public Map<Character, Map<Character, Double>> getScores() {
        return this.scoreMap;
    }

    /**
     * Rende il valore di score associato ad una determinata coppia di caratteri
     *
     * @param c1 Il primo carattere
     * @param c2 Il secondo carattere
     * @return Lo score associato alla coppia
     */
    public double getScore(char c1, char c2) throws InvalidAlphabetException {
        return this.scoreMap.get(c1).get(c2);
    }
    
    /**
     * Rende la Gap open penalty 
     * @return Gap Open Penalty
     */
    public double getGapOpenPenalty(){
        return this.gop;
    }

    /**
     * Rende la Gap Extension Penalty
     * @return Gap Extension Penalty
     */
    public double getGapExtensionPenalty(){
        return this.gep;
    }
    /**
     * Trasforma la matrice di score in una stringa
     * @return la matrice di score sottoforma di stringa
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        ArrayList<Double> scoreList = new ArrayList<>();
        for(char c : this.alph.alphabet){
             for(Double d : this.scoreMap.get(c).values()){
                 scoreList.add(d);
             }
        }
        double max = Math.abs(scoreList.get(0));
        for(int i = 0; i < scoreList.size(); i++){
            double test = Math.abs(scoreList.get(i));
            if(max < test){
                max = test;
                continue;
            }else{
                continue;
            }
        }
        String scoreLen = String.valueOf(String.valueOf(max).length() + 3);
        String format = "%+" + scoreLen + ".1f";
        String indexFormat = "%" + scoreLen + "s";
        
        Character[] bases = this.scoreMap.keySet().toArray(new Character[0]);
        Map<Character, Double>[] scores = this.scoreMap.values().toArray(new HashMap[0]);
        
        for(int i = 0; i < bases.length; i++){
            builder.append(String.format(indexFormat, bases[i]));
        }
        builder.append("\n");
        for(int i = 0; i < bases.length; i++){
            builder.append(String.format("%s", bases[i]));
            for(int j = 0; j < scores[i].values().size(); j++){
                builder.append(String.format(format, scores[i].values().toArray()[j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}

package bacci.giovanni.biogiova.alignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta una sequenza biologica.
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class Sequence {
    private Alphabet alph = null;
    private char[] seq = null;
    private String id = null;
    
    /**
     * Costruisce una sequenze data un determinato alfabeto.
     * @param alph l'alfabeto con cui creare la sequenze
     * @param seq la sequenza
     * @throws InvalidAlphabetException Se nella sequenza creata esitono 
     * caratteri non propri dell'alfabeto scelto
     */
    public Sequence(Alphabet alph, String seq) throws InvalidAlphabetException{
        this.alph = alph;
        this.seq = seq.toCharArray();
        List<Character> alphList = new ArrayList<>();
        for(char c : alph.alphabet){
            alphList.add(c);
        }
        for(char c : this.seq){
            if(alphList.contains(Character.toUpperCase(c))){
                continue;
            }else{
                throw new InvalidAlphabetException("Symbol not present "
                        + "in the Alphabet");
            }
        }
    }
    
    /**
     * Costruisce una sequenze data un determinato alfabeto.
     * @param alph l'alfabeto con cui creare la sequenze
     * @param seq la sequenza
     * @param id un id univoco della sequenza
     * @throws InvalidAlphabetException Se nella sequenza creata esitono 
     * caratteri non propri dell'alfabeto scelto
     */
    public Sequence(Alphabet alph, String seq, String id) throws InvalidAlphabetException{
        this.alph = alph;
        this.seq = seq.toCharArray();
        this.id = id;
        List<Character> alphList = new ArrayList<>();
        for(char c : alph.alphabet){
            alphList.add(c);
        }
        for(char c : this.seq){
            if(alphList.contains(Character.toUpperCase(c))){
                continue;
            }else{
                throw new InvalidAlphabetException("Symbol not present "
                        + "in the Alphabet");
            }
        }
    }
    
    /**
     * Trasforma la sequenza in una stringa contenente le basi
     * @return la sequenza a String
     */
    @Override
    public String toString(){
       return String.valueOf(seq);
    }
    
    
    /**
     * Rende la lunghezza della sequenza
     * @return la lunghezza della sequenza
     */
    public int length(){
        return seq.length;
    }
    
    /**
     * Rende l'id univoco della sequenza se è presente, altrimente rende null
     * @return l'id
     */
    public String getId(){
        return this.id;
    }
    
    /**
     * Rende la sequenza come un array di caratteri
     * @return la sequenza sotto forma di Array di caratteri
     */
    public char[] getSequence(){
        return this.seq;
    }
    
    /**
     * Rende l'alfabeto con cui è stata creata la sequenza
     * @return Alphabet con il quale è stata creata la sequenza
     */
    public Alphabet getAlphabet(){
        return this.alph;
    }
}

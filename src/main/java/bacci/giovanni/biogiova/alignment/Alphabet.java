package bacci.giovanni.biogiova.alignment;

/**
 * Classe che definisce un alfabeto delle sequenze da allineare, Le tipologie
 * di alfabeto sono quelle riportate in {@link AlphabetType}
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class Alphabet {
    public char[] alphabet = null;
    
    public Alphabet(AlphabetType type){
        this.alphabet = type.getAlphabet();
    }
    
    public int size(){
        return this.alphabet.length;
    }
}

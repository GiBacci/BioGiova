package bacci.giovanni.biogiova.alignment;

/**
 * Eccezione dovuta all'utilizzo di caratteri non propri dell'alfabeto scelto, 
 * oppure dovuto all'utilizzo di sequenze con diversi alfabeti per allineamenti.
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public class InvalidAlphabetException extends Exception {
       
    public InvalidAlphabetException(){
        super();
    }
    
    public InvalidAlphabetException(String message){
        super(message);
    }
}

package bacci.giovanni.biogiova.alignment;

/**
 * Eccezione che viene tirata quando si tenta di creare un'istanza della classe
 * {@link AlignedSequences} con una lista di sequenze in cui almeno una non ha 
 * la stessa lunghezza delle altre
 * @author giovanni
 */
public class DifferentLengthException extends Exception {
    
    public DifferentLengthException(){
        super();
    }
    
    public DifferentLengthException(String message){
        super(message);
    }
    
}

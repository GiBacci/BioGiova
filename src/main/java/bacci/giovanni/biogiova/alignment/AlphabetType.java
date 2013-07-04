package bacci.giovanni.biogiova.alignment;

/**
 * Elenco di tutti i possibili alfabeti in biologia (DNA, PROTEINE e RNA).
 * @author Giovanni Bacci {@link giovanni.bacci@unifi.it}
 */
public enum AlphabetType {
    DNA, PROTEIN, RNA;
    
    private final char[] dnaAlph = {'A','C','T','G','N','-'};
    private final char[] proteinAlph = {'A','R','N','D','C','G','E','Q','H','I',
        'L','K','M','F','P','S','T','W','Y','V','-'};
    private final char[] rnaAlph = {'A','C','U','G','N','-'};
    
    /**
     * Metodo che rende un array di caratteri pari all'alfabeto scelto
     * @return char[] comprendente l'alfabeto scelto
     */
    public char[] getAlphabet(){
        char[] arr = null;
        switch(this.toString()){
            case "DNA":  arr = dnaAlph;
                break;
            case "PROTEIN": arr = proteinAlph;
                break;
            case "RNA": arr = rnaAlph;
                break;
        }
        return arr;
    }
}

package bacci.giovanni.biogiova.sequencing;

public enum SequencesType {
	DNA, 
	RNA, 
	PROTEIN;

	private static final char[] DNA_ALPHABET = { 'a', 'c', 'g', 't', 'n', '-' };
	private static final char[] RNA_ALPHABET = { 'a', 'c', 'g', 'u', 'n', '-' };
	private static final char[] PROTEIN_ALPHABET = { 'a', 'r', 'n', 'd', 'c',
			'e', 'q', 'g', 'h', 'i', 'l', 'k', 'm', 'f', 'p', 's', 't', 'w',
			'y', 'v', 'u', 'o', 'b', 'z', 'j', 'x', '-' };
	
	
	public char[] getAlphabet(){
		if(this == DNA){
			return DNA_ALPHABET;
		}
		if(this == RNA){
			return RNA_ALPHABET;
		}
		if(this == PROTEIN){
			return PROTEIN_ALPHABET;
		}
		return null;
	}

}

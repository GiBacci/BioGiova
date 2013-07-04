package bacci.giovanni.biogiova.sequencing;

public abstract class AlignedSequences {
	
	/**
	 * Return a double character array representing the AlignedSequences sequences.
	 * @return a character double array
	 */
	public abstract char[][] getSequences();
	
	/**
	 * Must return an array of ids in the same order as the sequences.
	 * Ex: 
	 * <pre>
	 * {@code
	 * String id1 = "id1";
     * String sequence = "ACCGTACT";
     * String id2 = "id2;
     * String sequence = "TTTCGACT";
     * 
	 * getSequence() = {{A,C,C,G,T,A,C,T},
	 *                  {T,T,T,C,G,A,C,T}};
	 * getIds() = {id1,id2};                          
	 * }      
	 * </pre>
	 * @return
	 */
	public abstract String[] getIds();
	
	public abstract SequencesType getType();
}

package bacci.giovanni.biogiova.multialignment.matrices;

import bacci.giovanni.biogiova.sequencing.SequencesType;

/**
 * List of all possible matrix. Implemented.
 * @author <a href="http://www.unifi.it/dblage/CMpro-v-p-65.html">Giovanni Bacci</a>
 *
 */

public enum ScoreMatricesType {
	
	BLAST (SequencesType.DNA, "blast.txt"),
	IDENTITY (SequencesType.DNA, "identity.txt"), 
	IUB (SequencesType.DNA, "iub.txt"), 
	WEIGHT (SequencesType.DNA, "weight.txt"),
	IDENTITY_PENALTY(SequencesType.DNA, "identity_penalty.txt");
	
	private SequencesType type = null;
	private String name = null;
	
	private ScoreMatricesType(SequencesType type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public SequencesType getType() {
		return type;
	}
	
	
}

package bacci.giovanni.biogiova.multialignment;

import bacci.giovanni.biogiova.AlignmentDirections;

public class MovementTouple<AlignmentDirections, Integer> {
	
	public final AlignmentDirections dir;
	public final Integer mov;
	
	public MovementTouple(AlignmentDirections dir, Integer mov) {
		this.dir = dir;
		this.mov = mov;
	}
	
	
}

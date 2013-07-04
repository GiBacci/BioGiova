package bacci.giovanni.biogiova;

public enum AlignmentDirections {
	UP, LEFT, DIAGONAL;
	
	private double value = 0.0;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	
}

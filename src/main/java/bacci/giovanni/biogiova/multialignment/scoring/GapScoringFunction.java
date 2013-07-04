package bacci.giovanni.biogiova.multialignment.scoring;

public abstract class GapScoringFunction {
	
	private double gop = 0.0;
	private double gep = 0.0;
		
	public GapScoringFunction(double gop, double gep) {
		this.gop = gop;
		this.gep = gep;
	}

	abstract public double getScore(double initialScore, int movNumber);

	public double getGop() {
		return gop;
	}

	public void setGop(double gop) {
		this.gop = gop;
	}

	public double getGep() {
		return gep;
	}

	public void setGep(double gep) {
		this.gep = gep;
	}
}

package bacci.giovanni.biogiova.multialignment.scoring;

import java.io.InputStream;

import bacci.giovanni.biogiova.multialignment.matrices.ScoreMatricesType;

public class ScoreMatrixFactory {
	
	public static ScoreMatrix newInstance(ScoreMatricesType type){
		ScoreMatrix mat = null;
		InputStream in = type.getClass().getResourceAsStream(type.getName());
		mat = new LoadedScoreMatrix(in, type.getType());
		return mat;
	}
	
	
}

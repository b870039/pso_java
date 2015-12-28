package org.gandhim.pso;

public class testProblemSet implements PSOConstants{

	public static void main(String args[]) {
		Location location = null;
		double[] loc = new double[PROBLEM_DIMENSION];
		loc[0]=0.45;
		loc[1]=0.07;
		loc[2]=-0.2;
		loc[3]=-18.87;
		loc[4]=-0.07;
		loc[5]=-0.03;
		loc[6]=10.0;
		loc[7]=0.15;
		loc[8]=-29.45;
		loc[9]=0.08;
		loc[10]=0.02;
		loc[11]=0.54;
		loc[12]=0.28;
		loc[13]=0.28;
		loc[14]=0.0;
		location = new Location(loc);
		for(int j=0;j<PROBLEM_DIMENSION;j++){
			System.out.println("     Best " + j + ": " + location.getLoc()[j]);
		}
		System.out.println("     Value: " + ProblemSet.evaluate(location));
		
	}
}

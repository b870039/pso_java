package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import java.util.Random;
import java.util.Vector;

public class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	
	Random generator = new Random();
	
	public void execute() {
		initializeSwarm();
		updateFitnessList();
		
		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		int iteration=0;
		double w;
		double err = 9999;
		
		while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				if(t!=0){
					t=0;
				}
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				for(int j=0;j<PROBLEM_DIMENSION;j++){
					newVel[j] = (w * p.getVelocity().getPos()[j]) + 
								(r1 * C1) * (pBestLocation.get(i).getLoc()[j] - p.getLocation().getLoc()[j]) +
								(r2 * C2) * (gBestLocation.getLoc()[j] - p.getLocation().getLoc()[j]);
				}				
				
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				for(int j=0;j<PROBLEM_DIMENSION;j++){
					newLoc[j] = p.getLocation().getLoc()[j] + newVel[j];
				}
				
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
			
			err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0
			
			
			System.out.println("ITERATION " + iteration + ": ");

			for(int j=0;j<PROBLEM_DIMENSION;j++){
				System.out.println("     Best " + j + ": " + gBestLocation.getLoc()[j]);
			}

			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
			
			t++;
			iteration++;
			updateFitnessList();
		}
		
		System.out.println("\nSolution found at iteration " + (iteration - 1) + ", the solutions is:");
		for(int j=0;j<PROBLEM_DIMENSION;j++){
			System.out.println("     Best " + j + ": " + gBestLocation.getLoc()[j]);
		}

		System.out.println("----------------------------------------------");
		System.out.println(gBestLocation.getLoc()[0] + "\t" +gBestLocation.getLoc()[3] + "\t" +gBestLocation.getLoc()[6] + "\t" +gBestLocation.getLoc()[8] + "\t" +gBestLocation.getLoc()[11] + "\t" +gBestLocation.getLoc()[14]);
		System.out.println(gBestLocation.getLoc()[1] + "\t" +gBestLocation.getLoc()[4] + "\t" +gBestLocation.getLoc()[7] + "\t" +gBestLocation.getLoc()[9] + "\t" +gBestLocation.getLoc()[12]);
		System.out.println(gBestLocation.getLoc()[2] + "\t" +gBestLocation.getLoc()[5] + "\t" + "0" + "\t" +gBestLocation.getLoc()[10] + "\t" +gBestLocation.getLoc()[13]);
		
	}
	
	public void initializeSwarm() {
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) {
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.LOC_PAlpha_LOW + generator.nextDouble() * (ProblemSet.LOC_PAlpha_HIGH - ProblemSet.LOC_PAlpha_LOW);
			loc[1] = ProblemSet.LOC_PBeta_LOW + generator.nextDouble() * (ProblemSet.LOC_PBeta_HIGH - ProblemSet.LOC_PBeta_LOW);
			loc[2] = ProblemSet.LOC_PTheta_LOW + generator.nextDouble() * (ProblemSet.LOC_PTheta_HIGH - ProblemSet.LOC_PTheta_LOW);
			loc[3] = ProblemSet.LOC_QAlpha_LOW + generator.nextDouble() * (ProblemSet.LOC_QAlpha_HIGH - ProblemSet.LOC_QAlpha_LOW);
			loc[4] = ProblemSet.LOC_QBeta_LOW + generator.nextDouble() * (ProblemSet.LOC_QBeta_HIGH - ProblemSet.LOC_QBeta_LOW);
			loc[5] = ProblemSet.LOC_QSTheta_LOW + generator.nextDouble() * (ProblemSet.LOC_QSTheta_HIGH - ProblemSet.LOC_QSTheta_LOW);
			loc[6] = ProblemSet.LOC_RAlpha_LOW + generator.nextDouble() * (ProblemSet.LOC_RAlpha_HIGH - ProblemSet.LOC_RAlpha_LOW);
			loc[7] = ProblemSet.LOC_RBeta_LOW + generator.nextDouble() * (ProblemSet.LOC_RBeta_HIGH - ProblemSet.LOC_RBeta_LOW);
			loc[8] = ProblemSet.LOC_SAlpha_LOW + generator.nextDouble() * (ProblemSet.LOC_SAlpha_HIGH - ProblemSet.LOC_SAlpha_LOW);
			loc[9] = ProblemSet.LOC_SBeta_LOW + generator.nextDouble() * (ProblemSet.LOC_SBeta_HIGH - ProblemSet.LOC_SBeta_LOW);
			loc[10] = ProblemSet.LOC_QSTheta_LOW + generator.nextDouble() * (ProblemSet.LOC_QSTheta_HIGH - ProblemSet.LOC_QSTheta_LOW);
			loc[11] = ProblemSet.LOC_TAlpha_LOW + generator.nextDouble() * (ProblemSet.LOC_TAlpha_HIGH - ProblemSet.LOC_TAlpha_LOW);
			loc[12] = ProblemSet.LOC_TBeta_LOW + generator.nextDouble() * (ProblemSet.LOC_TBeta_HIGH - ProblemSet.LOC_TBeta_LOW);
			loc[13] = ProblemSet.LOC_TTheta_LOW + generator.nextDouble() * (ProblemSet.LOC_TTheta_HIGH - ProblemSet.LOC_TTheta_LOW);
			loc[14] = ProblemSet.LOC_DC_LOW + generator.nextDouble() * (ProblemSet.LOC_DC_HIGH - ProblemSet.LOC_DC_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			for(int j=0;j<PROBLEM_DIMENSION;j++){
				vel[j] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			}
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList() {
		for(int i=0; i<SWARM_SIZE; i++) {
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}
}

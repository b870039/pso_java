package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is an interface to keep the configuration for the PSO
// you can modify the value depends on your needs

public interface PSOConstants {
	int SWARM_SIZE = 30;
	int MAX_ITERATION = 200;
	int PROBLEM_DIMENSION = 15;
	double C1 = 2.0;
	double C2 = 2.0;
	double W_UPPERBOUND = 1.20;
	double W_LOWERBOUND = 0.0;
}

package org.gandhim.pso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the problem to be solved
// to find an x and a y that minimize the function below:
// f(x, y) = (2.8125 - x + x * y^4)^2 + (2.25 - x + x * y^2)^2 + (1.5 - x + x*y)^2
// where 1 <= x <= 4, and -1 <= y <= 1

// you can modify the function depends on your needs
// if your problem space is greater than 2-dimensional space
// you need to introduce a new variable (other than x and y)

public class ProblemSet {
	public static final double LOC_X_LOW = 1;
	public static final double LOC_X_HIGH = 10;
	public static final double LOC_Y_LOW = -0.12;
	public static final double LOC_Y_HIGH = 0.12;
	public static final double LOC_Theta_LOW = -0.12;
	public static final double LOC_Theta_HIGH = 0.12;
	public static final double VEL_LOW = -0.01;
	public static final double VEL_HIGH = 0.1;
	public static final String str = "Single_ECG.txt";
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location) {
		double result = 0;
		double PAlpha = location.getLoc()[0]; // the "pqrst" part of the location
		double PBeta = location.getLoc()[1]; 
		double PTheta = location.getLoc()[2];
		double QAlpha = location.getLoc()[3];
		double QBeta = location.getLoc()[4]; 
		double QTheta = location.getLoc()[5];
		double RAlpha = location.getLoc()[6];
		double RBeta = location.getLoc()[7]; 
		double SAlpha = location.getLoc()[8];
		double SBeta = location.getLoc()[9]; 
		double STheta = location.getLoc()[10];
		double TAlpha = location.getLoc()[11];
		double TBeta = location.getLoc()[12]; 
		double TTheta = location.getLoc()[13]; 
		double DC = location.getLoc()[14];
		double[] Single_ECG;
		Single_ECG = readData(str);
		
		//排除非法解
		if(PTheta>0){
			PTheta*=-1;
		}
		if(QTheta>0){
			QTheta*=-1;
		}
		if(STheta<0){
			STheta*=-1;
		}
		if(TTheta<0){
			TTheta*=-1;
		}
		if(PTheta>QTheta){
			double tmp;
			tmp=PTheta;
			PTheta=QTheta;
			QTheta=tmp;
		}
		if(STheta>TTheta){
			double tmp;
			tmp=STheta;
			STheta=TTheta;
			TTheta=tmp;
		}
		if(PTheta<-0.5)
			PTheta=-0.5;
		if(QTheta<-0.15)
			QTheta=-0.15;
		if(TTheta>0.5)
			TTheta=0.5;
		if(STheta>0.15)
			STheta=0.15;
		if(PBeta>0.5)
			PBeta=0.5;
		if(QBeta>0.5)
			QBeta=0.5;
		if(RBeta>0.5)
			RBeta=0.5;
		if(SBeta>0.5)
			SBeta=0.5;
		if(TBeta>0.5)
			TBeta=0.5;
		if(PBeta<-0.5)
			PBeta=-0.5;
		if(QBeta<-0.5)
			QBeta=-0.5;
		if(RBeta<-0.5)
			RBeta=-0.5;
		if(SBeta<-0.5)
			SBeta=-0.5;
		if(TBeta<-0.5)
			TBeta=-0.5;

		if(PAlpha<0)
			PAlpha=-PAlpha;
		if(QAlpha>0)
			QAlpha=-QAlpha;
		if(RAlpha<0)
			RAlpha=-RAlpha;
		if(SAlpha>0)
			SAlpha=-SAlpha;
		if(TAlpha<0)
			TAlpha=-TAlpha;
		
		if(PAlpha>RAlpha)
			PAlpha/=2;
		if(TAlpha>RAlpha)
			TAlpha/=2;
		
		
		location.getLoc()[0]=PAlpha;
		location.getLoc()[1]=PBeta;
		location.getLoc()[2]=PTheta;
		location.getLoc()[3]=QAlpha;
		location.getLoc()[4]=QBeta;
		location.getLoc()[5]=QTheta;
		location.getLoc()[6]=RAlpha;
		location.getLoc()[7]=RBeta;
		location.getLoc()[8]=SAlpha;
		location.getLoc()[9]=SBeta;
		location.getLoc()[10]=STheta;
		location.getLoc()[11]=TAlpha;
		location.getLoc()[12]=TBeta;
		location.getLoc()[13]=TTheta;
		//修正結束
		for(int i=0;i<Single_ECG.length;i++){
			double Gofn,PWave,QWave,RWave,SWave,TWave;
			PWave=PAlpha*Math.pow(PBeta, 2)*Math.exp(-Math.pow(((Math.PI*(double)2*(double)i)/Single_ECG.length)-Math.PI-Math.PI*(double)2*PTheta, 2)/((double)2*Math.pow(PBeta, 2)));
			QWave=QAlpha*Math.pow(QBeta, 2)*Math.exp(-Math.pow(((Math.PI*(double)2*(double)i)/Single_ECG.length)-Math.PI-Math.PI*(double)2*QTheta, 2)/((double)2*Math.pow(QBeta, 2)));
			RWave=RAlpha*Math.pow(RBeta, 2)*Math.exp(-Math.pow(((Math.PI*(double)2*(double)i)/Single_ECG.length)-Math.PI, 2)/((double)2*Math.pow(RBeta, 2)));
			SWave=SAlpha*Math.pow(SBeta, 2)*Math.exp(-Math.pow(((Math.PI*(double)2*(double)i)/Single_ECG.length)-Math.PI-Math.PI*(double)2*STheta, 2)/((double)2*Math.pow(SBeta, 2)));
			TWave=TAlpha*Math.pow(TBeta, 2)*Math.exp(-Math.pow(((Math.PI*(double)2*(double)i)/Single_ECG.length)-Math.PI-Math.PI*(double)2*TTheta, 2)/((double)2*Math.pow(TBeta, 2)));
			Gofn=PWave+QWave+RWave+SWave+TWave+DC;
			//System.out.println(PWave+"\t"+QWave+"\t"+RWave+"\t"+SWave+"\t"+TWave+"\t"+Gofn);
			//System.out.println(Gofn);
			result+=Math.pow(Gofn-Single_ECG[i],2);
		}
		result=((double)result/Single_ECG.length);
		
		return result;
	}
	
	public static  double[] readData(String record) {
		double[] sig0 = null;
    	List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(record));
	    	sig0 = new double[lines.size()-1];
	    	int index=0;
	    	for(String line:lines){
	    		double tmpD = Double.parseDouble(line);
	    		if(index==0)
	    			index++;
	    		else
	    			sig0[index++-1]=tmpD;
	    		//System.out.printf("%f\n",tmpD);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
        return sig0;
    }
}

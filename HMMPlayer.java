package duckhunt.support;


import DoubleMatrix;
import HMM;
import Trainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HMMPlayer {
	
	// You will be given three matrices (in this order); transition matrix, emission matrix, and initial state probability distribution
	
	private DoubleMatrix transitionMatrix;
	private DoubleMatrix emissionMatrix;
	private DoubleMatrix initialState;	
	
	public HMMPlayer() {
		
	}
	
	public void hmm1() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String row1 = reader.readLine();
		String row2 = reader.readLine();
		String row3 = reader.readLine();
		
		this.transitionMatrix = DoubleMatrix.parseFromRow(row1);
		this.emissionMatrix = DoubleMatrix.parseFromRow(row2);
		this.initialState = DoubleMatrix.parseFromRow(row3);
		
		System.err.println(this.transitionMatrix.toString());
		System.err.println(this.emissionMatrix.toString());
		System.err.println(this.initialState.toString());
		
		DoubleMatrix state = this.initialState.dotProduct(this.transitionMatrix);
		DoubleMatrix prod = state.dotProduct(emissionMatrix);
		System.out.println(prod.toOutput(true));
	}
	
	public void hmm2() throws IOException {
		
		String[] rows = new String[4];
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
		for(int i = 0; i < rows.length; i++)
			rows[i] = reader.readLine();
		
		/*
		 * Umbrella world:
		 */		
//		
//		rows[0] = "2 2 0.7 0.3 0.3 0.7";
//		rows[1] = "2 2 0.9 0.1 0.2 0.8";
//		rows[2] = "1 2 0.5 0.5";
//		rows[3] = "5 0 0 1 0 0";
		
//		rows[0] = "4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0"; 
//		rows[1] = "4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9"; 
//		rows[2] = "1 4 1.0 0.0 0.0 0.0"; 
//		rows[3] = "8 0 1 2 3 0 1 2 3";
	
//		rows[0] = "4 4 0.6 0.1 0.1 0.2 0.0 0.3 0.2 0.5 0.8 0.1 0.0 0.1 0.2 0.0 0.1 0.7";
//		rows[1] = "4 4 0.6 0.2 0.1 0.1 0.1 0.4 0.1 0.4 0.0 0.0 0.7 0.3 0.0 0.0 0.1 0.9";
//		rows[2] = "1 4 0.4 0.2 0.1 0.3";
//		rows[3] = "4 3 0 0 2";
//		
		this.transitionMatrix = DoubleMatrix.parseFromRow(rows[0]);
		this.emissionMatrix = DoubleMatrix.parseFromRow(rows[1]);
		this.initialState = DoubleMatrix.parseFromRow(rows[2]);		
		
		String[] spl = rows[3].split(" ");
		int[] emissions = new int[spl.length-1];
		for(int i = 1; i < spl.length; i++)
			emissions[i-1] = Integer.parseInt(spl[i]);		
		
		HMM initial = new HMM(initialState, transitionMatrix, emissionMatrix);
		DoubleMatrix alphaState = initial.alpha(emissions);	

		double[] alpha_scale = initial.alphaScales();
		double sum = 0.0d;
		for(int i = 0; i < transitionMatrix.height; i++)
		{			
			sum += alphaState.get(alphaState.height-1, i);	
		}
		
		double scalsum = 0; 
		for(int i = 0; i < alphaState.height; i++)
			scalsum += Math.log(alpha_scale[i]);
		
		double prob = Math.log(sum) - scalsum;
		System.out.println(Math.exp(prob));	
	}	
	

		
	public void hmm3() throws IOException {
		String[] rows = new String[4];
//	
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
//		for(int i = 0; i < rows.length; i++)
//			rows[i] = reader.readLine();		

	
		/**
		 * Kattis-input
		 */		
		/*
		rows[0] = "4 4 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.1 0.1 0.1 0.0 0.8 0.8 0.1 0.1 0.0"; 
		rows[1] = "4 4 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.0 0.0 0.0 0.9 0.1 0.1 0.0 0.0 0.9"; 
		rows[2] = "1 4 1.0 0.0 0.0 0.0"; 
		rows[3] = "4 1 1 2 2"; 
		*/
		
		/*
		 * Umbrella world:
		 */		
		/*
		rows[0] = "2 2 0.7 0.3 0.3 0.7";
		rows[1] = "2 2 0.9 0.1 0.2 0.8";
		rows[2] = "1 2 0.5 0.5";
		rows[3] = "5 0 0 1 0 0";
		*/
		
		/* 
		 * My own test
		 */		
		
//		rows[0] = "3 3 0.1 0.2 0.7 0.7 0.0 0.3 0.0 0.7 0.3";
//		rows[1] = "3 2 1.0 0.0 0.0 1.0 1.0 0.0";
//		rows[2] = "1 3 1.0 0.0 0.0";
//		rows[3] = "6 0 1 0 1 0 0";
		
		
		/*
		 * Toy example: http://homepages.ulb.ac.be/~dgonze/TEACHING/viterbi.pdf
		 */		
		
//		rows[0] = "2 2 0.5 0.5 0.4 0.6";
//		rows[1] = "2 4 0.2 0.3 0.3 0.2 0.3 0.2 0.2 0.3";
//		rows[2] = "1 2 0.5 0.5";
//		rows[3] = "9 2 2 1 0 1 3 2 1 1";
		
		
		/*
		 * 	http://www.cs.usfca.edu/~pfrancislyon/courses/640/viterbi.pdf
		 */
		rows[0] = "3 3 0.25 0.5 0.25 0.25 0.25 0.5 0.5 0.5 0.0";
		rows[1] = "3 4 1.0 0.0 0.0 0.0 0.25 0.5 0.0 0.5 0.25 0.25 0.25 0.25";
		rows[2] = "1 3 0.25 0.5 0.25";
		rows[3] = "3 1 1 2";
		
		
		this.transitionMatrix = DoubleMatrix.parseFromRow(rows[0]);
		this.emissionMatrix = DoubleMatrix.parseFromRow(rows[1]);
		this.initialState = DoubleMatrix.parseFromRow(rows[2]);		
		
		String[] spl = rows[3].split(" ");
		int[] emissions = new int[spl.length-1];
		for(int i = 1; i < spl.length; i++)
			emissions[i-1] = Integer.parseInt(spl[i]);	
		
		HMM initial = new HMM(initialState, transitionMatrix, emissionMatrix);
		
		int[] states = initial.viterbi(emissions);
		for(int state : states)
			System.out.print(state + " ");
		System.out.println();
		
	}
	

	
	public void hmm4() throws IOException {
		String[] rows = new String[4];
		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));		
//		for(int i = 0; i < rows.length; i++)
//			rows[i] = reader.readLine();
		
		
		// Kattis Example 1
		rows[0] = "4 4 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4"; 
		rows[1] = "4 4 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4"; 
		rows[2] = "1 4 0.241896 0.266086 0.249153 0.242864"; 
		rows[3] = "1000 0 1 2 3 3 0 0 1 1 1 2 2 2 3 0 0 0 1 1 1 2 3 3 0 0 0 1 1 1 2 3 3 0 1 2 3 0 1 1 1 2 3 3 0 1 2 2 3 0 0 0 1 1 2 2 3 0 1 1 2 3 0 1 2 2 2 2 3 0 0 1 2 3 0 1 1 2 3 3 3 0 0 1 1 1 1 2 2 3 3 3 0 1 2 3 3 3 3 0 1 1 2 2 3 0 0 0 0 0 0 0 0 0 1 1 1 1 1 2 2 2 3 3 3 3 0 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3 3 0 1 2 3 0 1 1 1 2 3 0 1 1 2 2 2 2 2 3 0 1 1 1 2 2 2 2 3 0 0 0 0 0 1 1 1 1 2 2 3 3 0 1 2 3 3 0 0 0 0 0 0 1 1 2 2 3 0 0 1 1 1 1 1 1 2 3 3 0 0 1 1 1 2 3 0 0 1 2 3 0 1 1 2 3 3 0 0 0 1 2 3 3 3 0 1 1 1 1 2 3 3 3 3 3 3 0 1 2 2 2 2 2 2 3 0 1 1 1 2 2 3 3 3 3 0 1 2 3 0 0 0 1 1 2 2 3 0 0 0 0 0 0 0 1 2 2 2 3 3 3 3 0 0 1 2 2 2 3 3 3 0 0 1 2 2 3 0 0 0 0 1 1 1 2 3 3 3 3 3 3 3 3 0 1 2 3 0 0 1 2 3 3 3 0 0 0 0 0 1 1 1 1 2 3 0 0 0 1 2 2 3 3 0 0 0 1 1 1 1 1 2 3 3 3 3 0 1 1 1 2 2 3 0 1 2 3 3 3 3 0 0 0 0 1 2 3 3 0 1 2 2 3 3 0 0 1 1 2 3 3 0 1 2 2 3 3 3 0 0 1 1 2 3 3 3 3 0 0 1 1 2 3 3 0 1 2 3 0 1 1 2 2 3 0 1 2 3 3 0 1 1 1 2 2 2 3 3 0 0 1 1 1 1 1 2 3 3 3 0 1 1 2 2 2 2 3 3 0 0 1 2 3 0 1 1 2 2 2 2 3 0 0 1 2 2 3 0 0 0 0 0 1 1 1 2 3 0 0 1 2 3 3 0 0 0 1 2 2 2 3 3 0 0 0 1 2 2 2 2 2 3 0 1 1 2 3 0 0 1 1 1 2 2 3 0 0 0 0 1 1 1 2 2 3 0 1 1 1 2 2 2 3 3 0 0 1 2 2 3 3 3 0 1 1 2 3 0 0 0 0 0 1 2 2 2 3 3 3 0 0 0 1 2 3 0 1 1 2 3 3 3 0 1 2 2 2 3 0 0 1 1 1 1 2 3 3 0 0 0 0 1 2 3 3 3 0 0 0 1 1 2 3 0 1 1 1 1 2 2 2 2 2 2 3 0 0 0 0 1 2 2 2 2 3 0 1 2 2 3 0 1 2 3 0 1 2 3 0 0 0 1 1 2 2 3 3 0 1 1 1 1 2 2 3 3 0 1 1 1 2 2 2 3 3 3 0 1 1 2 3 3 0 1 2 3 0 0 0 0 1 2 3 0 0 0 0 0 0 1 2 2 3 3 0 0 1 2 3 0 1 2 2 3 0 0 0 1 1 2 2 2 2 2 3 3 3 3 3 0 1 2 2 3 3 3 3 3 0 0 1 1 2 2 3 0 0 1 2 2 3 3 3 0 0 0 1 2 2 2 2 3 3 0 1 2 3 0 0 1 1 1 2 2 3 0 0 1 1 2 2 2 3 3 0 0 1 1 1 1 1 2 3 3 3 0 1 2 2 2 2 3 3 3 3 3 3 0 0 0 0 0 0 1 2 3 0 0 1 1 1 2 3 0 0 1 1 2 2 2 2 3 3 3 0 1 1 2 2 2 3 3 0 0 0 0 0 0 1 2 2 3 3 0 0 0 0 0 0 1 2 3 3 3 0 1 1 1 2 2 2 2 2 3 3 3 0 1 2 2 2 3 3 3 3 0 0 0 0 1 2 3 3 3 3 3 3 0 0 1 1 1 1 2 3 0 1 2 3 0 1 1 2 3 3 3 0 0 0 0 1 1 2 3 3 3 3 0 0 1 1 1 2 2 2 2 2 2 3 3 0 0 0 1 2 3 0 0 1 1 2 2 3 3 3 3 3 0 0 1 2 2 2 2 3 0 0 1 1 1 1 1 2 3 3 0 0 1 1 1 2 3 3 3 0 0";
		
		// Kattis Example 2
//		rows[0] = "4 4 0.2 0.4 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.4 0.2";
//		rows[1] = "4 4 0.7 0.1 0.1 0.1 0.1 0.7 0.1 0.1 0.1 0.1 0.7 0.1 0.1 0.1 0.1 0.7";
//		rows[2] = "1 4 1.0 0.0 0.0 0.0"; 
//		rows[3] = "1000 1 0 1 0 2 3 0 1 0 1 2 3 2 0 1 0 3 2 3 2 3 2 1 0 1 0 1 2 3 2 0 2 1 0 1 3 2 3 2 3 2 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1 2 3 2 0 1 0 1 2 3 2 3 0 3 2 1 0 3 2 1 2 3 0 1 0 1 2 0 3 0 1 0 3 0 1 2 3 1 2 3 2 1 0 1 0 3 2 3 2 1 2 1 0 2 1 3 2 3 2 3 1 0 1 0 3 2 3 2 0 1 0 2 1 0 1 0 3 1 0 1 0 2 1 0 3 2 3 2 3 2 3 0 1 2 1 0 1 0 1 0 3 2 3 2 0 1 3 2 3 2 3 0 1 0 3 2 3 2 3 0 1 3 2 3 1 0 1 0 3 1 0 1 2 3 0 1 0 3 2 3 0 1 0 1 2 1 0 3 0 1 0 1 3 2 3 1 0 1 2 1 0 3 2 1 0 2 3 1 2 3 2 3 2 3 2 3 0 3 0 1 2 3 2 1 3 2 0 3 2 3 2 3 2 1 0 1 3 0 1 3 2 3 2 0 3 2 3 2 3 2 3 0 1 0 1 2 3 2 3 2 1 0 1 0 3 0 1 0 2 3 0 1 0 1 0 1 2 1 0 1 0 1 3 2 3 2 3 2 3 0 2 1 2 3 2 3 2 0 1 0 1 0 1 0 3 2 3 2 3 0 1 0 1 0 2 3 2 3 2 3 2 3 2 1 0 2 3 2 3 0 1 2 3 2 1 0 1 2 0 3 1 0 1 0 3 0 2 3 1 0 1 2 1 2 3 0 3 0 1 0 3 2 1 0 1 0 3 2 0 1 0 1 2 3 0 3 2 1 0 3 2 0 1 0 1 0 1 0 1 0 1 2 0 1 3 2 3 2 3 0 2 1 0 3 2 1 0 1 0 1 0 2 3 2 1 0 1 0 1 0 1 2 3 0 1 2 0 1 0 1 0 1 0 1 0 3 2 1 0 1 0 1 0 1 2 3 2 3 0 1 0 1 0 3 2 3 2 1 2 1 0 1 0 1 2 3 2 3 2 3 2 3 0 3 0 1 0 3 2 3 2 3 2 1 2 3 2 3 2 3 0 1 3 0 2 1 0 1 0 1 3 0 1 0 1 0 1 0 3 2 3 2 1 0 1 3 1 0 1 0 3 0 1 0 2 3 2 3 2 3 2 3 2 1 2 3 2 3 2 1 0 1 0 1 2 3 2 3 2 1 0 1 2 1 0 1 3 0 3 2 3 2 3 0 1 0 1 2 3 2 1 0 1 0 1 3 2 1 0 3 2 1 0 3 2 3 2 0 1 0 1 2 3 0 2 3 2 3 2 1 0 1 2 3 2 1 0 1 3 2 0 1 2 3 0 2 3 2 3 0 1 0 1 2 3 2 0 1 0 3 2 0 1 0 1 0 1 0 1 0 1 0 1 3 2 3 2 1 2 3 2 1 0 1 0 1 0 1 0 3 0 1 0 1 0 3 0 1 0 1 2 3 0 1 0 3 0 1 0 1 0 1 2 3 0 1 0 3 0 1 0 1 2 3 0 1 0 1 2 1 3 0 1 0 1 0 1 0 3 2 3 2 3 2 1 0 1 0 1 3 0 3 0 1 2 3 1 3 2 3 2 1 3 1 2 1 0 1 0 1 0 2 1 0 1 0 1 2 3 2 1 0 2 3 2 3 2 3 2 3 0 3 2 3 0 1 2 3 2 3 2 3 2 3 1 0 1 0 1 0 1 2 0 3 0 3 2 0 3 2 1 0 1 0 1 0 1 2 3 2 3 2 1 2 3 0 1 0 3 2 0 3 2 0 1 0 1 0 3 0 1 0 1 2 3 2 3 2 1 0 1 0 1 2 3 2 3 2 3 2 1 0 1 0 1 0 3 0 1 2 1 0 3 2 3 2 3 2 0 3 2 3 2 3 0 1 2 3 1 0 1 0 3 2 3 2 1 2 3 0 3 2 3 2 3 2 1 2 3 2 3 2 1 2 3 2 3 2 3 2 1 0 1 0 1 0 3 2 3 0 3 2 3 2 3 0 1 2 1 0 3 0 3 2 3 2 3 2 0 1 2 3 0 1 0 1 2 0 1 0 1 0 1 0 1 3 2 1 0 1 0 1 0 1 0 3 2 3 2 1 0 1 0 1 0 3 2 1 0 1 0 1 0 1 3 2 3 0 3 0 1"; 

		// Kattis Example 1 (Fred modified)
//		rows[0] = "4 4 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4"; 
//		rows[1] = "4 4 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4 0.2 0.2 0.2 0.2 0.4"; 
//		rows[2] = "1 4 0.241896 0.266086 0.249153 0.242864"; 
//		rows[3] = "10 0 1 2 3 3 0 0 1 1 1";
//		
		
//		rows[0] = "2 2 0.5 0.5 0.3 0.7";
//		rows[1] = "2 2 0.3 0.7 0.8 0.2";
//		rows[2] = "1 2 0.2 0.8";
//		rows[3] = "2 0 0";
		
//		rows[0] = "2 2 0.7 0.3 0.3 0.7";
//		rows[1] = "2 2 0.9 0.1 0.2 0.8";
//		rows[2] = "1 2 0.5 0.5";
//		rows[3] = "5 0 0 1 0 0";
				
//		rows[0] = "2 2 0.9 0.1 0.0 1.0";
//		rows[1] = "2 1 0.3 0.7";
//		rows[2] = "1 2 1.0 0.0";
//		rows[3] = "5 0 0 0 0";

		this.transitionMatrix = DoubleMatrix.parseFromRow(rows[0]);
		this.emissionMatrix = DoubleMatrix.parseFromRow(rows[1]);
		this.initialState = DoubleMatrix.parseFromRow(rows[2]);		
		
		String[] spl = rows[3].split(" ");
		int[] emissions = new int[spl.length-1];
		for(int i = 1; i < spl.length; i++)
			emissions[i-1] = Integer.parseInt(spl[i]);		
		
//		algo = new Algorithms(emissions, transitionMatrix, emissionMatrix, initialState);
		HMM initial = new HMM(initialState, transitionMatrix, emissionMatrix);
		Trainer trainer = new Trainer(initial);
		
		HMM trainedModel = trainer.TrainModel(emissions);
//		System.err.println(Math.exp(trainedModel.logProb()));
		//		algo.BaumWelch(100);
		
//		System.out.println(algo.getInitialState());
//		System.out.println(algo.getTransitionMatrix().toOutput(true));
//		System.out.println(algo.getEmissionsMatrix().toOutput(true));
		System.out.println(trainedModel.getA().toOutput(true));
		System.out.println(trainedModel.getB().toOutput(true));
	}
	
	
	public static void main(String[] args) throws IOException {
		HMMPlayer player = new HMMPlayer();
		
		
		player.hmm4();
	}
}

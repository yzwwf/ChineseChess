package learningtoevaluate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import chessabstraction.Situation;
import neuralnetworks.NeuralNetwork;
import neuralnetworks.NeuralNetworkImplement;

/*
 * An evaluator for evaluating the score of a 
 * situation, and can learn and improve the precision
 * of the evaluation.
 */
public class LearningEvaluator {
	private NeuralNetwork neuralNetwork;
	private String neuralNetworkSavePath;
	private static final int NUM_FEATURES = 9*10*15+2;
	private PrintWriter trainRecorder;
	public LearningEvaluator(String filePath) {
		this.neuralNetworkSavePath = filePath;
		this.loadNeuralNetwork();
		try {
			this.trainRecorder = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.neuralNetworkSavePath+".examples", true)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// returns true if success
	private void loadNeuralNetwork() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(this.neuralNetworkSavePath));
			this.neuralNetwork = (NeuralNetwork) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (this.neuralNetwork == null) {
			// 1. input dimension: 
			//    the board has 9*10 positions, each position might be empty, or 
			//    occupied by some piece (1 out of 14 possibilities)
			// 2. a hidden layer
			// 3. an output layer with only one dimension
			//this.neuralNetwork = new NeuralNetworkImplement(NUM_FEATURES, new int[] {10,1});
			//this.neuralNetwork = new NeuralNetworkImplement(NUM_FEATURES, new int[] {1});
			this.neuralNetwork = new NeuralNetworkImplement(NUM_FEATURES, new int[] {10, 10, 10, 1});
		}
	
	}
	
	// check whether this neural network is valid
	// 1. not null
	// 2. input dimension is 9*10*14*2
	private boolean checkNeuralNetwork() {
		return true;
	}
	private boolean saveNeuralNetwork() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.neuralNetworkSavePath));
			oos.writeObject(this.neuralNetwork);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;	
	}
	
	public boolean saveLearnedState() {
		return this.saveNeuralNetwork();
	}
	
	private double[] extractFeature(Situation situation) {
		double [] result = new double[NUM_FEATURES];
		for (int i = 0; i < 10; i++) {
			for(int j=0; j<9; j++) {
				int piece = situation.getChess(i, j);
				int featureIndex=(i*9+j)*15+piece;
				result[featureIndex] = 1;
			}
		}
		if (situation.isA_turn()) {
			result[result.length-2] = 1;
		} else {
			result[result.length-1] = 1;
		}
		return result;
	}
	
	public double evaluate(Situation situation) {
		double [] featureVec = this.extractFeature(situation);
		double [] resultVec = this.neuralNetwork.predict(featureVec);
		return resultVec[0];	
	}
	public void learn(Situation situation, double expectedScore) {
		double [] featureVec = this.extractFeature(situation);
		double [] expectedOutput = new double[] {expectedScore};
		this.neuralNetwork.trainOnce(featureVec, expectedOutput);
		
		// TODO tmp code, record some learning examples
//		StringBuilder sb = new StringBuilder();
//		sb.append(expectedOutput[0]);
//		sb.append(" ");
//		for (double i : featureVec) {
//			sb.append(i);
//			sb.append(" ");
//		}
//		this.trainRecorder.println(sb.toString());
//		this.trainRecorder.flush();
		//System.out.println(sb.toString());
		
	}
	
	// returns the absolute value of output diff
	public double learnWithoutRecord(double[] featureVec, double expectedScore, double delta) {
		double[] expectedOutputs =new double[] {expectedScore};
		double realOutput = this.neuralNetwork.predict(featureVec)[0];
		this.neuralNetwork.trainOnce(featureVec, expectedOutputs, delta);
		return Math.abs(expectedScore-realOutput);
	}
	public double learnWithoutRecord(double[] featureVec, double expectedScore) {
		return this.learnWithoutRecord(featureVec, expectedScore, 1);
	}
}

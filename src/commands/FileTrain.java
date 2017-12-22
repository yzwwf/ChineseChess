package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import learningtoevaluate.LearningEvaluator;

public class FileTrain {
	public static void main(String[] args) throws FileNotFoundException {
		LearningEvaluator learningEvaluator = new LearningEvaluator("learning_evaluator_for_online_learning_player_1.model");
		//load training examples
		List<double[]> inputs = new ArrayList<double[]>();
		List<Double> expectedOutputs = new ArrayList<Double>();
		Scanner in = new Scanner(new File("learning_evaluator_for_online_learning_player_1.model.examples"));
		while(in.hasNext()) {
			//System.out.println("line ");
			String line = in.nextLine();
			String [] subStrs = line.trim().split(" ");
			if (subStrs.length == 0) {
				break;
			}
			expectedOutputs.add(Double.valueOf(subStrs[0]));
			double[] input = new double[subStrs.length-1];
			for (int i=1; i<subStrs.length; i++) {
				//System.out.println("substring : " + subStrs[i]);
				input[i-1] = Double.valueOf(subStrs[i]);
			}
			inputs.add(input);
		}
		System.out.println("load data complete");
		//
		int iterations = 500;
		for (int iteration = 0; iteration < iterations; iteration++) {
			System.out.println("begin to train, iteration "+iteration);
			double totalDiffs = 0;
			for (int i=0; i<inputs.size(); i++) {
				double[] input = inputs.get(i);
				double expectedOutput = expectedOutputs.get(i);
				double diff = learningEvaluator.learnWithoutRecord(input, expectedOutput, 0.2);
				totalDiffs += diff;
			}
			System.out.println("total diff of this iteration : "+totalDiffs);
		}
		learningEvaluator.saveLearnedState();
	}
}

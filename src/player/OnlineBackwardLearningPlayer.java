package player;

import chessabstraction.Move;
import chessabstraction.MoveGenerator;
import chessabstraction.Situation;
import learningtoevaluate.LearningEvaluator;

public class OnlineBackwardLearningPlayer implements IPlayer {

	private LearningEvaluator learningEvaluator;

	OnlineBackwardLearningPlayer() {
		this.learningEvaluator = new LearningEvaluator("default_learning_evaluator.model");
	}

	public OnlineBackwardLearningPlayer(String playerName) {
		this.learningEvaluator = new LearningEvaluator("learning_evaluator_for_" + playerName + ".model");
	}

	private static double runningDiff = -1;

	@Override
	public Move decideMove(Situation situation) {
		// int depth = 2 + (int)(Math.random()*2);
		int depth = 2;
		Result result;
		if (situation.isA_turn()) {
			result = this.alphaBetaMaxMin(situation, -0.1, 1.1, depth);
		} else {
			result = this.alphaBetaMinMax(situation, -0.1, 1.1, depth);
		}
//		if (result.getMove() == null) {
//			System.out.println("result move is null");
//		}
		double predict = this.learningEvaluator.evaluate(situation);
		double diff = Math.abs(predict - result.getScore());
		if (runningDiff < 0) {
			runningDiff = diff;
		} else {
			runningDiff = 0.999 * runningDiff + 0.001 * diff;
		}
		if (result.getScore()<0.1 ||result.getScore()>0.9) {
			System.out.println("selected move : " + result.getMove() + "   score : " + result.getScore() + " predict : "
				+ predict + " runningDiff : " + runningDiff);
		}
		//this.learningEvaluator.learn(situation, result.getScore());
		// train
		if (result.getScore() == 0 || result.getScore() == 1) {
			double score = result.getScore();
			while(true) {
				this.learningEvaluator.learn(situation, score);
				if (situation.isInit()) break;
				score = this.learningEvaluator.evaluate(situation);
				situation.goBack();
			}
		}
		if (Math.random() < 0.05) {
			this.learningEvaluator.saveLearnedState();
		}
		return result.getMove();

	}

	private double evaluate(Situation situation) {
		int winner = situation.winner();
		if (winner == 0) {// A wins
			return 1;
		} else if (winner == 1) { // B wins
			return 0;
		} else {
			return this.learningEvaluator.evaluate(situation);
		}
	}

	private Result alphaBetaMaxMin(Situation situation, double currMaxBound, double currMinBound, int depthLeft) {
		if (!situation.isA_turn()) {
			System.out.println("illegal : not a turn in max min");
		}
		if (depthLeft == 0) {
			return new Result(null, evaluate(situation));
		}
		int situationWinner = situation.winner();
		if (situationWinner == 0) {
			return new Result(null, 1);
		} else if (situationWinner == 1) {
			return new Result(null, 0);
		}

		// If there is no possible move, the max side loses the
		// game. The default socre, which is Double.MIN_VALUE, is
		// reasonable.
		double currentMaxScore = -0.1;
		Move currentBestMove = null;

		for (Move move : MoveGenerator.listAllMoves(situation)) {
			// try move and get the resulting score
			situation.move(move);
			double tmpScore = alphaBetaMinMax(situation, Math.max(currMaxBound, currentMaxScore), currMinBound,
					depthLeft - 1).getScore();
			situation.goBack();
			if (tmpScore > currentMaxScore) {
				// update the result of this max node
				currentBestMove = move;
				currentMaxScore = tmpScore;
				if (currentMaxScore > currMinBound) {
					// this max node will not contribute to
					// ancestor min nodes, no need to explore
					break;
				}
			}

		}
		return new Result(currentBestMove, currentMaxScore);
	}

	private Result alphaBetaMinMax(Situation situation, double currMaxBound, double currMinBound, int depthLeft) {
		if (situation.isA_turn()) {
			System.out.println("illegal situation: a turn in min max");
		}
		if (depthLeft == 0) {
			return new Result(null, evaluate(situation));
		}
		int situationWinner = situation.winner();
		if (situationWinner == 0) {
			return new Result(null, 1);
		} else if (situationWinner == 1) {
			return new Result(null, 0);
		}

		double currentMinScore = 1.1;
		Move currentBestMove = null;

		for (Move move : MoveGenerator.listAllMoves(situation)) {
			situation.move(move);
			double tmpScore = alphaBetaMaxMin(situation, currMaxBound, Math.max(currMinBound, currentMinScore),
					depthLeft - 1).getScore();
			situation.goBack();
			if (tmpScore < currentMinScore) {
				currentBestMove = move;
				currentMinScore = tmpScore;
				if (currentMinScore <= currMaxBound) {
					// this min node will not contribute to
					// ancestor max nodes, no need to explore
					break;
				}
			}
		}
		return new Result(currentBestMove, currentMinScore);
	}

	private static class Result {
		double score;
		Move move;

		Result(double score, Move move) {
			this.score = score;
			this.move = move;
		}

		Result(Move move, double score) {
			this.score = score;
			this.move = move;
		}

		double getScore() {
			return this.score;
		}

		Move getMove() {
			return this.move;
		}
	}
}

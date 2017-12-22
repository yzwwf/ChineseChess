package player;

import chessabstraction.Move;
import chessabstraction.MoveGenerator;
import chessabstraction.Situation;
import learningtoevaluate.LearningEvaluator;

public class SimpleLearningPlayer implements IPlayer {

	// search depth, default to 3
		private int searchDepth = 3;
		
		private static final double IMPOSSIBLE_MAX = 1.1;
		private static final double IMPOSSIBLE_MIN = -0.1;
		
		private LearningEvaluator learningEvaluator;
		
		public SimpleLearningPlayer() {
			this.learningEvaluator = new LearningEvaluator("learning_evaluator_for_online_learning_player_1.model");
		}
		
		private static double runningDiff = -1;
		@Override
		public Move decideMove(Situation situation) {
			AlphaBetaResult result;
			if (situation.isA_turn()) {
				// it's time for A to move
				result = this.alphaBetaMaxMin(situation, this.IMPOSSIBLE_MIN, this.IMPOSSIBLE_MAX, searchDepth); 
			} else {
				// it's time for B to move
				result = this.alphaBetaMinMax(situation, this.IMPOSSIBLE_MIN, this.IMPOSSIBLE_MAX, searchDepth);
			}
			double predict = this.learningEvaluator.evaluate(situation);
			double diff = Math.abs(predict - result.getScore());
			if (runningDiff < 0) {
				runningDiff = diff;
			} else {
				runningDiff = 0.999 * runningDiff + 0.001 * diff;
			}
			

			this.learningEvaluator.learn(situation, result.getScore());
			
			double post_predict = this.learningEvaluator.evaluate(situation);
			System.out.printf("move: %s score: %.6f, predict:%.6f, predict':%.6f running diff: %.6f\n", result.getMove(), result.getScore(), predict, post_predict, runningDiff);
			
			if (Math.random() < 0.05) {
				this.learningEvaluator.saveLearnedState();
			}
			System.out.println("score : " + result.getScore());
			return result.getMove();
		}
		
		double evaluate(Situation situation) {
//			if (situation.winner() == 0) {
//				// A wins
//				return 5000;
//			} else if (situation.winner() == 1) {
//				// B wins
//				return -5000;
//			} else {
//				return situation.getA_Score();
//			}
			return situation.getA_Score();
		}
		
		// Result of an alpha-beta search
		private static class AlphaBetaResult {
			// the best (either max or min) score that can 
			// be achieved after N steps of moves
			private double score; 
			// the move that should be performed in the next
			// step in order to achieve the best score
			private Move move;
			AlphaBetaResult(Move move, double score) {
				this.move = move;
				this.score = score;
			}
			Move getMove() { return this.move; }
			double getScore() { return this.score; }
		}
		
		private AlphaBetaResult alphaBetaMaxMin(Situation situation, double currMaxBound, double currMinBound, int depthLeft) {
			if (!situation.isA_turn()) {
				System.out.println("illegal : not a turn in max min");
			}
			if (depthLeft == 0) {
				return new AlphaBetaResult(null, evaluate(situation));
			} 
			int situationWinner=situation.winner();
			if (situationWinner==0||situationWinner==1) {
				return new AlphaBetaResult(null, situation.getA_Score());
			}
			
			// If there is no possible move, the max side loses the
			// game. The default socre, which is Double.MIN_VALUE, is
			// reasonable.
			double currentMaxScore = this.IMPOSSIBLE_MIN;
			Move currentBestMove = null;
			
			for (Move move : MoveGenerator.listAllMoves(situation)) {
				//try move and get the resulting score
				situation.move(move);
				double tmpScore = alphaBetaMinMax(situation, Math.max(currMaxBound, currentMaxScore), currMinBound, depthLeft-1).getScore();
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
			return new AlphaBetaResult(currentBestMove, currentMaxScore);
		}
		
		private AlphaBetaResult alphaBetaMinMax(Situation situation, double currMaxBound, double currMinBound, int depthLeft) {
			if (situation.isA_turn()) {
				System.out.println("illegal situation: a turn in min max");
			}
			if (depthLeft == 0) {
				return new AlphaBetaResult(null, evaluate(situation));
			}
			int situationWinner=situation.winner();
			if (situationWinner==0||situationWinner==1) {
				return new AlphaBetaResult(null, situation.getA_Score());
			}
			
			double currentMinScore = this.IMPOSSIBLE_MAX;
			Move currentBestMove = null;
			
			for (Move move : MoveGenerator.listAllMoves(situation)) {
				situation.move(move);
				double tmpScore = alphaBetaMaxMin(situation, currMaxBound, Math.max(currMinBound, currentMinScore), depthLeft-1).getScore();
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
			return new AlphaBetaResult(currentBestMove, currentMinScore);
		}
}

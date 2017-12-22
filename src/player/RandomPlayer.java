package player;

import java.util.List;
import java.util.Random;

import chessabstraction.Move;
import chessabstraction.MoveGenerator;
import chessabstraction.Situation;

public class RandomPlayer implements IPlayer {
	Random rand = new Random();
	@Override
	public Move decideMove(Situation situation) {
		List<Move> moves = MoveGenerator.listAllMoves(situation);
		int index = rand.nextInt(moves.size());
		return moves.get(index);
	}

}

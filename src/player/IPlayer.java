package player;

import chessabstraction.Move;
import chessabstraction.Situation;

public interface IPlayer {
	// decide which move to go for the next step
	public Move decideMove(Situation situation);
}

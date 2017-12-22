package chessabstraction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MoveGenerator {
	static Rule rule = new Rule();
	
	
	
	public static List<Move> listAllMoves(Situation situation) {
		if (situation.isA_turn()) {
			return listAllMovesForA(situation);
		}else {
			return listAllMovesForB(situation);
		}
	}
	/**
	 * 为A方生成走棋步骤
	 * 先生成杀着，即把杀着排到前面，这样有利于剪枝的进行。
	 * 生成走棋步子的代码的效率对于整个程序的效率十分重要，如果改善一下数据结构，消除10*9的循环，效率
	 * 可能会提高一点。但是这个会稍微麻烦一点，需要改动基本的数据结构。所以这个改动会比较大。
	 * 
	 * @param situation
	 * @return
	 */
	public static List<Move> listAllMovesForA(Situation situation) {
		List<Move> moves = new LinkedList<Move>();

		int to_row, to_col;
		Move move;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				int chess = situation.getChess(i, j);
				if (chess >= 1 && chess <= 7) {
					switch (chess) {
					case Situation.A_CAR:
						for (int row = i + 1; row < 10; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
									break;
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int row = i - 1; row >= 0; row--) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
									break;
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int col = j + 1; col < 9; col++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
									break;
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int col = j - 1; col >= 0; col--) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
									break;
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}

						break;
					case Situation.A_HORSE:
						int[][] horse_directs = { { 1, 2 }, { 1, -2 },
								{ -1, 2 }, { -1, -2 }, { 2, 1 }, { 2, -1 },
								{ -2, 1 }, { -2, -1 } };
						for (int k = 0; k < horse_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + horse_directs[k][0]);
							move.setTo_col(j + horse_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}

						break;
					case Situation.A_ELEPHANT:
						int[][] elephant_directs = { { 2, 2 }, { 2, -2 },
								{ -2, 2 }, { -2, -2 } };
						for (int k = 0; k < elephant_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + elephant_directs[k][0]);
							move.setTo_col(j + elephant_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.A_GUARD:
						int[][] guard_directs = { { 1, 1 }, { 1, -1 },
								{ -1, 1 }, { -1, -1 } };
						for (int k = 0; k < guard_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + guard_directs[k][0]);
							move.setTo_col(j + guard_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.A_GENERAL:
						for (int row = 0; row <= 2; row++) {
							for (int col = 3; col <= 5; col++) {
								move = new Move();
								move.setChess_num(chess);
								move.setFrom_row(i);
								move.setFrom_col(j);
								move.setTo_row(row);
								move.setTo_col(col);
								if (rule.canMove(situation, move)) {
									int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
									if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
										moves.add(0, move);
									}else{
										moves.add(move);
									}
								}
							}
						}
						for (int row = 7; row <= 9; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.A_CANON:
						for (int row = 0; row < 10; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						for (int col = 0; col < 9; col++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.A_INFANTRY:
						int[][] infantry_directs = { { 1, 0 }, { -1, 0 },
								{ 0, 1 }, { 0, -1 } };
						for (int k = 0; k < infantry_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + infantry_directs[k][0]);
							move.setTo_col(j + infantry_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=8&&chess_eaten<=14){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					}
				}
			}
		}

		return moves;
	}
	
	/**
	 * 为B方生成走棋步骤
	 * @param situation
	 * @return
	 */
	public static List<Move> listAllMovesForB(Situation situation) {
		List<Move> moves = new ArrayList<Move>(80);

		Move move;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				int chess = situation.getChess(i, j);
				if (chess >= 8 && chess <= 14) {
					switch (chess) {
					case Situation.B_CAR:
						for (int row = i + 1; row < 10; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int row = i - 1; row >= 0; row--) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int col = j + 1; col < 9; col++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						for (int col = j - 1; col >= 0; col--) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							} else {
								break;
							}
						}
						break;
					case Situation.B_HORSE:
						int[][] horse_directs = { { 1, 2 }, { 1, -2 },
								{ -1, 2 }, { -1, -2 }, { 2, 1 }, { 2, -1 },
								{ -2, 1 }, { -2, -1 } };
						for (int k = 0; k < horse_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + horse_directs[k][0]);
							move.setTo_col(j + horse_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}

						break;
					case Situation.B_ELEPHANT:
						int[][] elephant_directs = { { 2, 2 }, { 2, -2 },
								{ -2, 2 }, { -2, -2 } };
						for (int k = 0; k < elephant_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + elephant_directs[k][0]);
							move.setTo_col(j + elephant_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.B_GUARD:
						int[][] guard_directs = { { 1, 1 }, { 1, -1 },
								{ -1, 1 }, { -1, -1 } };
						for (int k = 0; k < guard_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + guard_directs[k][0]);
							move.setTo_col(j + guard_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.B_GENERAL:
						for (int row = 0; row <= 2; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						for (int row = 7; row <= 9; row++) {
							for (int col = 3; col <= 5; col++) {
								move = new Move();
								move.setChess_num(chess);
								move.setFrom_row(i);
								move.setFrom_col(j);
								move.setTo_row(row);
								move.setTo_col(col);
								if (rule.canMove(situation, move)) {
									int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
									if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
										moves.add(0, move);
									}else{
										moves.add(move);
									}
								}
							}
						}
						break;
					case Situation.B_CANON:
						for (int row = 0; row < 10; row++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(row);
							move.setTo_col(j);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						for (int col = 0; col < 9; col++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i);
							move.setTo_col(col);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					case Situation.B_INFANTRY:
						int[][] infantry_directs = { { 1, 0 }, { -1, 0 },
								{ 0, 1 }, { 0, -1 } };
						for (int k = 0; k < infantry_directs.length; k++) {
							move = new Move();
							move.setChess_num(chess);
							move.setFrom_row(i);
							move.setFrom_col(j);
							move.setTo_row(i + infantry_directs[k][0]);
							move.setTo_col(j + infantry_directs[k][1]);
							if (rule.canMove(situation, move)) {
								int chess_eaten=situation.getChess(move.getTo_row(), move.getTo_col());
								if(chess_eaten>=1&&chess_eaten<=7){//如果是杀着，就将此步走法加入到列表前面
									moves.add(0, move);
								}else{
									moves.add(move);
								}
							}
						}
						break;
					}
				}
			}
		}

		return moves;
	}
}

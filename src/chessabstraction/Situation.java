package chessabstraction;

import java.util.List;
import java.util.Stack;

/*
 * 本类主要是用来作为棋盘局势的内部表示.
 * 一个棋盘局势可以用一个矩阵来表示。
 * 各个棋子的表示规定如下：
 * A方：
 * 	车 1	马2	象3	士4	将5	炮6	卒7
 * B方：
 *  车8	马9	象10	士11	将12	炮13	卒14
 * 
 * 矩阵与棋盘的正常放置方式对应，10行9列
 * 
 * 
 */
public class Situation {
	// 先定义一些公用常量
	public final static int A_CAR = 1, A_HORSE = 2, A_ELEPHANT = 3,
			A_GUARD = 4, A_GENERAL = 5, A_CANON = 6, A_INFANTRY = 7, B_CAR = 8,
			B_HORSE = 9, B_ELEPHANT = 10, B_GUARD = 11, B_GENERAL = 12,
			B_CANON = 13, B_INFANTRY = 14;

	// 棋局矩阵
	private int[][] situation_matrix;

	// 现在该哪一方走棋
	private boolean A_turn;

	//记录走棋步骤，用于回退
	private Stack<Record> recordStack=new Stack<Record>();
	
	//局面评估
	private double a_score=0.5;
	
	public void init(boolean aFirst) {
		this.initBoard();
		this.A_turn=aFirst;
	}
	
	public void initBoard() {
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < 9; j++)
				this.situation_matrix[i][j] = 0;
		this.situation_matrix[0][0] = this.situation_matrix[0][8] = A_CAR;
		this.situation_matrix[0][1] = this.situation_matrix[0][7] = A_HORSE;
		this.situation_matrix[0][2] = this.situation_matrix[0][6] = A_ELEPHANT;
		this.situation_matrix[0][3] = this.situation_matrix[0][5] = A_GUARD;
		this.situation_matrix[0][4] = A_GENERAL;
		this.situation_matrix[2][1] = this.situation_matrix[2][7] = A_CANON;
		this.situation_matrix[3][0] = this.situation_matrix[3][2] = this.situation_matrix[3][4] = this.situation_matrix[3][6] = this.situation_matrix[3][8] = A_INFANTRY;

		this.situation_matrix[9][0] = this.situation_matrix[9][8] = B_CAR;
		this.situation_matrix[9][1] = this.situation_matrix[9][7] = B_HORSE;
		this.situation_matrix[9][2] = this.situation_matrix[9][6] = B_ELEPHANT;
		this.situation_matrix[9][3] = this.situation_matrix[9][5] = B_GUARD;
		this.situation_matrix[9][4] = B_GENERAL;
		this.situation_matrix[7][1] = this.situation_matrix[7][7] = B_CANON;
		this.situation_matrix[6][0] = this.situation_matrix[6][2] = this.situation_matrix[6][4] = this.situation_matrix[6][6] = this.situation_matrix[6][8] = B_INFANTRY;
		this.recordStack.clear();
		this.a_score=0.5;
		this.A_turn = false;
	}

	public Situation() {
		this.situation_matrix = new int[10][9];
		this.A_turn = false;
	}

	/*
	 * 走一步
	 */
	public void move(Move mov) {
		if (new Rule().canMove(this, mov)) {
			// record move history
			this.record(mov);
			// update score
			this.changeA_Score(mov);
			// update board : set move src position and dst position
			this.situation_matrix[mov.getTo_row()][mov.getTo_col()] = this.situation_matrix[mov
					.getFrom_row()][mov.getFrom_col()];
			this.situation_matrix[mov.getFrom_row()][mov.getFrom_col()] = 0;
			// update a_turn : flip
			this.A_turn ^= true;
		}
	}

	private static final int[] GENERAL_ROWS = {0,1,2,7,8,9};
	/*
	 * 判断哪方胜。如果A方胜，返回0，如果B方胜，返回1，如果没有结束，返回2。异常情况下返回-1
	 * 判断的依据是老将是否还在。
	 */
	public int winner(){
		
		boolean a_general_exist=false;
		boolean b_general_exist=false;
		
		for (int j=3; j<=5; j++) {
			for (int i : GENERAL_ROWS) {
				if (this.situation_matrix[i][j]==A_GENERAL) {
					a_general_exist=true;
				} else if(this.situation_matrix[i][j]==B_GENERAL) {
					b_general_exist=true;
				}
			}
		}

		if(!a_general_exist&&b_general_exist){
			return 1;
		}else if(a_general_exist&&!b_general_exist){
			return 0;
		}else if(a_general_exist&&b_general_exist){
			return 2;
		}else{
			return -1;
		}
	}
	
	/*
	 * 此方法返回指定位置的棋子
	 */
	public int getChess(int i, int j) {
		if (i >= 0 && i < 10 && j >= 0 && j < 9) {
			return this.situation_matrix[i][j];
		} else {
			// 关于到底是返回-1还是返回0，这个现在暂时定为返回-1
			return -1;
		}
	}

	public boolean isA_turn() {
		return this.A_turn;
	}
	
	public double getA_Score(){
		return this.a_score;
	}
	
	// 每一个棋子的权重
	private double[] weights = { 0.023, 0.0105, 0.00485, 0.00485, 0.25, 0.0105, 0.00315, -0.023, -0.0105, -0.00485, -0.00485, -0.25, -0.0105, -0.00315 };
	private void changeA_Score(Move move){
		int eaten_chess=this.situation_matrix[move.getTo_row()][move.getTo_col()];
		if(eaten_chess!=0){
			this.a_score-=weights[eaten_chess-1];
		}
	}
	
	private void changeA_Score(Record record){
		int eaten_chess=record.getChess_eaten();
		if(eaten_chess!=0){
			this.a_score+=weights[eaten_chess-1];
		}
	}
	
	/*
	 * 记录下某一个走棋步骤
	 */
	private void record(Move move){
		Record record=new Record();
		record.setFrom_row(move.getFrom_row());
		record.setFrom_col(move.getFrom_col());
		record.setTo_row(move.getTo_row());
		record.setTo_col(move.getTo_col());
		record.setChess_eaten(this.situation_matrix[record.getTo_row()][record.getTo_col()]);
		this.recordStack.push(record);
	}
	
	
	
	/*
	 * 回退一步棋
	 */
	public void goBack(){
		if(this.recordStack.isEmpty())return;
		Record record=this.recordStack.pop();
		this.changeA_Score(record);
		this.situation_matrix[record.getFrom_row()][record.getFrom_col()]=this.situation_matrix[record.getTo_row()][record.getTo_col()];
		this.situation_matrix[record.getTo_row()][record.getTo_col()]=record.getChess_eaten();
		this.A_turn ^= true;
	}
	
	public boolean isInit() {
		return this.recordStack.isEmpty();
	}
	
	
	/*
	 * 重载Object.hashCode类
	 */
	@Override 
	public int hashCode(){
		return java.util.Arrays.toString(this.situation_matrix).hashCode();
	}
	
	
	/*
	 * 内部类，用于记录走棋步骤
	 */
	class Record{
		private int from_row;
		private int from_col;
		private int to_row;
		private int to_col;
		private int chess_eaten;
		
		public Record(){}

		public Record(int from_row, int from_col, int to_row, int to_col,
				int chess_eaten) {
			super();
			this.from_row = from_row;
			this.from_col = from_col;
			this.to_row = to_row;
			this.to_col = to_col;
			this.chess_eaten = chess_eaten;
		}
		
		public int getFrom_row() {
			return from_row;
		}

		public void setFrom_row(int from_row) {
			this.from_row = from_row;
		}

		public int getFrom_col() {
			return from_col;
		}

		public void setFrom_col(int from_col) {
			this.from_col = from_col;
		}

		public int getTo_row() {
			return to_row;
		}

		public void setTo_row(int to_row) {
			this.to_row = to_row;
		}

		public int getTo_col() {
			return to_col;
		}

		public void setTo_col(int to_col) {
			this.to_col = to_col;
		}

		public int getChess_eaten() {
			return chess_eaten;
		}

		public void setChess_eaten(int chess_eaten) {
			this.chess_eaten = chess_eaten;
		}
		
		
		
	}
}
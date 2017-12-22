package chessabstraction;

/*
 * 现在来说，此类中所包含的信息与Situation类相结合时，是有一些冗余的，
 * 因为当指定移动的开始位置之后，棋子也就已经确定了。但是从常理来看，
 * 设置这个字段也算是增加易读性，使得整个数据结构比较容易理解一些，自
 * 成体系。
 */
public class Move {
	private int chess_num;
	private int from_row;
	private int from_col;
	private int to_row;
	private int to_col;
	
	public Move(){}

	public Move(int chess_num, int from_row, int from_col, int to_row,
			int to_col) {
		super();
		this.chess_num = chess_num;
		this.from_row = from_row;
		this.from_col = from_col;
		this.to_row = to_row;
		this.to_col = to_col;
	}
	
	@Override 
	public String toString() {
		String [] pieceNames = {"A车", "A马", "A相", "A士", "A将", "A炮", "A卒", "B车", "B马", "B相", "B士", "B将", "B炮", "B卒"};
		return pieceNames[chess_num - 1] + "\t(" + from_row + "," +from_col+")-->(" + to_row+","+to_col+")";
	}

	public int getChess_num() {
		return chess_num;
	}

	public void setChess_num(int chess_num) {
		this.chess_num = chess_num;
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
}

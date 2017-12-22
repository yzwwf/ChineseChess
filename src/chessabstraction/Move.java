package chessabstraction;

/*
 * ������˵������������������Ϣ��Situation������ʱ������һЩ����ģ�
 * ��Ϊ��ָ���ƶ��Ŀ�ʼλ��֮������Ҳ���Ѿ�ȷ���ˡ����Ǵӳ���������
 * ��������ֶ�Ҳ���������׶��ԣ�ʹ���������ݽṹ�Ƚ��������һЩ����
 * ����ϵ��
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
		String [] pieceNames = {"A��", "A��", "A��", "Aʿ", "A��", "A��", "A��", "B��", "B��", "B��", "Bʿ", "B��", "B��", "B��"};
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

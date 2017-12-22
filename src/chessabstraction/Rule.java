package chessabstraction;

public class Rule {

	/*
	 * �ж�һ���߲��Ƿ�Ϸ�,����Ͱ����������е����е��߲�����
	 */
	public boolean canMove(Situation situation, Move move) {
		boolean legal = true;
		// ���ȣ��ж�move�������ļ��������Ƿ�������
		// ������������ʼλ�û���Ŀ��λ�ã������ڵ�col��row��Ӧ�����㣺
		// 0<=col<9 , 0<=row<10
		// ���ң����ӵĴ�������Ӧ����1~14֮��
		if (move.getFrom_col() < 0 || move.getFrom_col() >= 9
				|| move.getFrom_row() < 0 || move.getFrom_row() >= 10
				|| move.getTo_col() < 0 || move.getTo_col() >= 9
				|| move.getTo_row() < 0 || move.getTo_row() >= 10
				|| move.getChess_num() < 1 || move.getChess_num() > 14) {
			//System.out.println("���Ӳ������Ϸ�������1~14��Χ֮��");
			return false;
		}
		
		//���move��situation�е���Ϣ��һ�£�Ҳ���ط�
		if(move.getChess_num()!=situation.getChess(move.getFrom_row(), move.getFrom_col())){
			//System.out.println("move��situation����Ϣ��һ��");
			return false;
		}

		//�����ʼλ�������λ����ͬ��Ҳ���Ϸ�
		if(move.getFrom_col()==move.getTo_col()&&move.getFrom_row()==move.getTo_row()){
			//System.out.println("��ʼλ�������λ����ͬ");
			return false;
		}
		//���Ŀ��λ�����м��������ӣ���Ҳ���Ϸ�
		int tempChess=situation.getChess(move.getTo_row(), move.getTo_col());
		if(tempChess!=0){
			if(tempChess<=7&&move.getChess_num()<=7)return false;
			if(tempChess>7&&move.getChess_num()>7)return false;
		}
		
		//�����Ǿ����ձ��ԵĹ���������Ҫ�ֲ�ͬ�����ӽ��й����ж�
		switch(move.getChess_num()){
		case Situation.A_CAR://A���ĳ�
			if(!this.judgeCarMove(situation, move))return false;
			break;//this is break of case 1
		case Situation.A_HORSE:
			if(!this.judgeHorseMove(situation, move))return false;
			break;
		case Situation.A_ELEPHANT:
			if(!this.judgeElephantMove(situation, move))return false;
			break;
		case Situation.A_GUARD:
			if(!this.judgeGuardMove(situation, move))return false;
			break;
		case Situation.A_GENERAL:
			if(!this.judgeGeneralMove(situation, move))return false;
			break;
		case Situation.A_CANON:
			if(!this.judgeCanonMove(situation, move))return false;
			break;
		case Situation.A_INFANTRY:
			if(!this.judgeInfantryMove(situation, move))return false;
			break;
		case Situation.B_CAR:
			if(!this.judgeCarMove(situation, move))return false;
			break;
		case Situation.B_HORSE:
			if(!this.judgeHorseMove(situation, move))return false;
			break;
		case Situation.B_ELEPHANT:
			if(!this.judgeElephantMove(situation, move))return false;
			break;
		case Situation.B_GUARD:
			if(!this.judgeGuardMove(situation, move))return false;
			break;
		case Situation.B_GENERAL:
			if(!this.judgeGeneralMove(situation, move))return false;
			break;
		case Situation.B_CANON:
			if(!this.judgeCanonMove(situation, move))return false;
			break;
		case Situation.B_INFANTRY:
			//System.out.println("�ж�B������");
			if(!this.judgeInfantryMove(situation, move))return false;
			break;
		}
		
		
		return true;

	}
	
	/*
	 * �жϳ������ߺϷ���
	 */
	private boolean judgeCarMove(Situation situation, Move move){
		//System.out.println("�жϳ����߷�,���("+move.getFrom_row()+","+move.getFrom_col()+")�յ�("+move.getTo_row()+","+move.getTo_col()+")");
		//���ȣ�������ֱ��
		if(move.getFrom_col()!=move.getTo_col()&&move.getFrom_row()!=move.getTo_row()){
			//System.out.println("����ֱ��");
			return false;
		}
		//���⣬�ӳ����㵽�������������ĵ��У�������������ͽ����㣩������������
		int directCol,directRow;
		if(move.getFrom_col()==move.getTo_col()){
			directCol=0;
			if(move.getFrom_row()>move.getTo_row()){
				directRow=-1;
			}else{
				directRow=1;
			}
		}else{
			directRow=0;
			if(move.getFrom_col()>move.getTo_col()){
				directCol=-1;
			}else{
				directCol=1;
			}
		}
		for(int i=1;directCol*i+move.getFrom_col()!=move.getTo_col()||directRow*i+move.getFrom_row()!=move.getTo_row();i++){
			if(situation.getChess(directRow*i+move.getFrom_row(), directCol*i+move.getFrom_col())!=0){
				//System.out.println("�м�������");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * �ж������ߵĺϷ���
	 */
	private boolean judgeHorseMove(Situation situation, Move move){
		//���ȣ�Ҫȷ����һ�����ա���
		int col_diff=move.getTo_col()-move.getFrom_col();
		int row_diff=move.getTo_row()-move.getFrom_row();
		if(!(((col_diff==1||col_diff==-1)&&(row_diff==2||row_diff==-2))||((col_diff==2||col_diff==-2)&&(row_diff==1||row_diff==-1)))){
			return false;
		}
		
		//���⣬�����С����ȡ�����
		if(col_diff==2){
			if(situation.getChess(move.getFrom_row(), move.getFrom_col()+1)!=0){
				return false;
			}
		}
		if(col_diff==-2){
			if(situation.getChess(move.getFrom_row(), move.getFrom_col()-1)!=0){
				return false;
			}
		}
		if(row_diff==2){
			if(situation.getChess(move.getFrom_row()+1, move.getFrom_col())!=0){
				return false;
			}
		}
		if(row_diff==-2){
			if(situation.getChess(move.getFrom_row()-1, move.getFrom_col())!=0){
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * �ж�������ߵĺϷ���
	 */
	private boolean judgeElephantMove(Situation situation, Move move){
		//System.out.println("judgeElephantMove() called**************");
		//���ȣ�Ҫ��һ�������
		if(Math.abs(move.getTo_col()-move.getFrom_col())!=2||Math.abs(move.getTo_row()-move.getFrom_row())!=2){
			//System.out.println("���߲�������");
			return false;
		}
		//���⣬��Ҫ�ж�λ��,��ǰ��Ӧ����ĺϷ�λ����
		int[][]ALegalPosition={{0,2},{2,0},{4,2},{2,4},{0,6},{2,8},{4,6}};
		int[][]BLegalPosition={{9,2},{7,0},{5,2},{7,4},{5,6},{7,8},{9,6}};
		int[][]legalPosition=(move.getChess_num()==Situation.A_ELEPHANT)?ALegalPosition:BLegalPosition;
		
		boolean from_is_in=false,to_is_in=false;
		for(int i=0;i<legalPosition.length;i++){
			if(move.getFrom_row()==legalPosition[i][0]&&move.getFrom_col()==legalPosition[i][1]){
				//System.out.println("��ʼλ�úϷ�");
				from_is_in=true;
				break;
			}
		}
		for(int i=0;i<legalPosition.length;i++){
			if(move.getTo_row()==legalPosition[i][0]&&move.getTo_col()==legalPosition[i][1]){
				//System.out.println("����λ�úϷ�");
				to_is_in=true;
				break;
			}
		}
		if(!(from_is_in&&to_is_in))
			return false;
		//���⣬�����ۡ��ϲ���������
		if(situation.getChess((move.getFrom_row()+move.getTo_row())/2, (move.getFrom_col()+move.getTo_col())/2)!=0)return false;
		return true;
	}
	
	/*
	 * �ж�ʿ�����ߵĺϷ���
	 */
	private boolean judgeGuardMove(Situation situation, Move move){
		//��������б·��
		if(!(Math.abs(move.getTo_row()-move.getFrom_row())==1&&Math.abs(move.getTo_col()-move.getFrom_col())==1)){
			return false;
		}
		//���⣬�������ھŹ�֮�ڣ����Ҫ��Ϊ�췽���ڷ����ֱ��ж�
		if(move.getChess_num()==Situation.A_GUARD){//A��
			if(!(move.getFrom_row()>=0&&move.getFrom_row()<=2&&move.getFrom_col()>=3&&move.getFrom_col()<=5&&move.getTo_row()>=0&&move.getTo_row()<=2&&move.getTo_col()>=3&&move.getTo_col()<=5)){
				return false;
			}
		}else{
			if(!(move.getFrom_row()>=7&&move.getFrom_row()<=9&&move.getFrom_col()>=3&&move.getFrom_col()<=5&&move.getTo_row()>=7&&move.getTo_row()<=9&&move.getTo_col()>=3&&move.getTo_col()<=5)){
				return false;
			}
		}
		
		return true;
	}
	/*
	 * �жϽ������ߵĺϷ���
	 */
	private boolean judgeGeneralMove(Situation situation, Move move){
		//��������һ��������߷�����������Ͻ������������ֱ�ӳԵ��Է����Ͻ�
		if(move.getChess_num()==Situation.A_GENERAL){
			if(situation.getChess(move.getTo_row(), move.getTo_col())==Situation.B_GENERAL){//���ȣ�Ŀ��Ӧ�ǶԷ��Ͻ�
				if(move.getTo_col()==move.getFrom_col()){//���⣬��Ҫ��ֱ��
					//ͳ����·��������
					int num_chesses=0;
					int direct_row;
					if(move.getFrom_row()>move.getTo_row())direct_row=-1;
					else direct_row=1;
					for(int i=1;move.getFrom_row()+direct_row*i!=move.getTo_row();i++){
						if(situation.getChess(move.getFrom_row()+i*direct_row, move.getFrom_col())!=0){
							num_chesses=1;
							break;
						}
					}
					if(num_chesses==0){//���У������Ͻ�֮�䲻Ӧ����������
						return true;
					}
				}
			}
		}else if(move.getChess_num()==Situation.B_GENERAL){
			if(situation.getChess(move.getTo_row(), move.getTo_col())==Situation.A_GENERAL){
				if(move.getTo_col()==move.getFrom_col()){
					//ͳ����·��������
					int num_chesses=0;
					int direct_row;
					if(move.getFrom_row()>move.getTo_row())direct_row=-1;
					else direct_row=1;
					for(int i=1;move.getFrom_row()+direct_row*i!=move.getTo_row();i++){
						if(situation.getChess(move.getFrom_row()+i*direct_row, move.getFrom_col())!=0){
							num_chesses=1;
							break;
						}
					}
					if(num_chesses==0){
						return true;
					}
				}
			}
		}
		//���ȣ�������ֻ��һ��
		if(move.getFrom_col()==move.getTo_col()){
			if(Math.abs(move.getTo_row()-move.getFrom_row())!=1){
				return false;
			}
		}else if(move.getFrom_row()==move.getTo_row()){
			if(Math.abs(move.getTo_col()-move.getFrom_col())!=1){
				return false;
			}
		}else{
			return false;
		}
		//���⣬�������ھŹ���
		if(move.getChess_num()==Situation.A_GENERAL){//A��
			if(!(move.getFrom_row()>=0&&move.getFrom_row()<=2&&move.getFrom_col()>=3&&move.getFrom_col()<=5&&move.getTo_row()>=0&&move.getTo_row()<=2&&move.getTo_col()>=3&&move.getTo_col()<=5)){
				return false;
			}
		}else{
			if(!(move.getFrom_row()>=7&&move.getFrom_row()<=9&&move.getFrom_col()>=3&&move.getFrom_col()<=5&&move.getTo_row()>=7&&move.getTo_row()<=9&&move.getTo_col()>=3&&move.getTo_col()<=5)){
				return false;
			}
		}
		return true;
	}
	/*
	 * �ж��ڵ����ߵĺϷ���
	 */
	private boolean judgeCanonMove(Situation situation, Move move){
		//���ȣ�������ֱ��
		if(move.getFrom_col()!=move.getTo_col()&&move.getFrom_row()!=move.getTo_row()){
			return false;
		}
		//���⣬������λ�����Ƿ������ӣ����Ƿ��ǳԲ�������Ϊ�����������
		int directCol,directRow;
		if(move.getFrom_col()==move.getTo_col()){
			directCol=0;
			if(move.getFrom_row()>move.getTo_row()){
				directRow=-1;
			}else{
				directRow=1;
			}
		}else{
			directRow=0;
			if(move.getFrom_col()>move.getTo_col()){
				directCol=-1;
			}else{
				directCol=1;
			}
		}
		int totalChesses=0;
		for(int i=1;directCol*i+move.getFrom_col()!=move.getTo_col()||directRow*i+move.getFrom_row()!=move.getTo_row();i++){
			if(situation.getChess(directRow*i+move.getFrom_row(), directCol*i+move.getFrom_col())!=0){
				totalChesses++;
			}
		}
		if(situation.getChess(move.getTo_row(), move.getTo_col())==0){
			//���û�г��ӵĻ�����·���������ӳ���
			if(totalChesses>0)
				return false;
		}else{
			//������ӣ�����·���ҽ���һ�����ӳ���
			if(totalChesses!=1){
				return false;
			}
		}
		
		
		return true;
	}
	/*
	 * �ж�������ߵĺϷ���
	 */
	private boolean judgeInfantryMove(Situation situation, Move move){
		//���ȣ�����������һ������
		if(move.getFrom_col()==move.getTo_col()){
			if(Math.abs(move.getFrom_row()-move.getTo_row())!=1)return false;
		}else if(move.getFrom_row()==move.getTo_row()){
			if(Math.abs(move.getFrom_col()-move.getTo_col())!=1)return false;
		}else{
			return false;
		}
		
		//��Ҫ�ֺ�ڷ��ֱ��ж�
		if(move.getChess_num()==Situation.A_INFANTRY){//A������
			if(move.getTo_row()<move.getFrom_row())//�䲻�ܺ���
				return false;
			if(move.getFrom_row()==move.getTo_row()&&move.getFrom_row()<=4)//�ڹ���ǰ����ƽ��
				return false;
		}else{//B������
			if(move.getTo_row()>move.getFrom_row())
				return false;
			if(move.getFrom_row()==move.getTo_row()&&move.getFrom_row()>=5)
				return false;
		}
	
		return true;
	}
	
}

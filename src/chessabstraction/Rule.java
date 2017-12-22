package chessabstraction;

public class Rule {

	/*
	 * 判断一个走步是否合法,这里就包含了象棋中的所有的走步规则
	 */
	public boolean canMove(Situation situation, Move move) {
		boolean legal = true;
		// 首先，判断move中所述的几个参数是否有意义
		// 即，无论是起始位置或是目的位置，其所在的col和row都应该满足：
		// 0<=col<9 , 0<=row<10
		// 而且，棋子的代表数字应该在1~14之间
		if (move.getFrom_col() < 0 || move.getFrom_col() >= 9
				|| move.getFrom_row() < 0 || move.getFrom_row() >= 10
				|| move.getTo_col() < 0 || move.getTo_col() >= 9
				|| move.getTo_row() < 0 || move.getTo_row() >= 10
				|| move.getChess_num() < 1 || move.getChess_num() > 14) {
			//System.out.println("棋子参数不合法，不在1~14范围之内");
			return false;
		}
		
		//如果move与situation中的信息不一致，也返回否
		if(move.getChess_num()!=situation.getChess(move.getFrom_row(), move.getFrom_col())){
			//System.out.println("move与situation中信息不一致");
			return false;
		}

		//如果开始位置与结束位置相同，也不合法
		if(move.getFrom_col()==move.getTo_col()&&move.getFrom_row()==move.getTo_row()){
			//System.out.println("开始位置与结束位置相同");
			return false;
		}
		//如果目的位置上有己方的棋子，则也不合法
		int tempChess=situation.getChess(move.getTo_row(), move.getTo_col());
		if(tempChess!=0){
			if(tempChess<=7&&move.getChess_num()<=7)return false;
			if(tempChess>7&&move.getChess_num()>7)return false;
		}
		
		//以上是具有普遍性的规则，下面需要分不同的棋子进行规则判断
		switch(move.getChess_num()){
		case Situation.A_CAR://A方的车
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
			//System.out.println("判断B方的卒");
			if(!this.judgeInfantryMove(situation, move))return false;
			break;
		}
		
		
		return true;

	}
	
	/*
	 * 判断车的行走合法性
	 */
	private boolean judgeCarMove(Situation situation, Move move){
		//System.out.println("判断车的走法,起点("+move.getFrom_row()+","+move.getFrom_col()+")终点("+move.getTo_row()+","+move.getTo_col()+")");
		//首先，必须是直线
		if(move.getFrom_col()!=move.getTo_col()&&move.getFrom_row()!=move.getTo_row()){
			//System.out.println("不是直线");
			return false;
		}
		//另外，从出发点到结束点所经过的点中（不包括出发点和结束点），不能有棋子
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
				//System.out.println("中间有棋子");
				return false;
			}
		}
		return true;
	}
	
	/*
	 * 判断马行走的合法性
	 */
	private boolean judgeHorseMove(Situation situation, Move move){
		//首先，要确保是一个“日”字
		int col_diff=move.getTo_col()-move.getFrom_col();
		int row_diff=move.getTo_row()-move.getFrom_row();
		if(!(((col_diff==1||col_diff==-1)&&(row_diff==2||row_diff==-2))||((col_diff==2||col_diff==-2)&&(row_diff==1||row_diff==-1)))){
			return false;
		}
		
		//另外，不能有“绊腿”现象
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
	 * 判断象的行走的合法性
	 */
	private boolean judgeElephantMove(Situation situation, Move move){
		//System.out.println("judgeElephantMove() called**************");
		//首先，要是一个“田”字
		if(Math.abs(move.getTo_col()-move.getFrom_col())!=2||Math.abs(move.getTo_row()-move.getFrom_row())!=2){
			//System.out.println("行走不是田字");
			return false;
		}
		//另外，还要判断位置,即前后都应在象的合法位置上
		int[][]ALegalPosition={{0,2},{2,0},{4,2},{2,4},{0,6},{2,8},{4,6}};
		int[][]BLegalPosition={{9,2},{7,0},{5,2},{7,4},{5,6},{7,8},{9,6}};
		int[][]legalPosition=(move.getChess_num()==Situation.A_ELEPHANT)?ALegalPosition:BLegalPosition;
		
		boolean from_is_in=false,to_is_in=false;
		for(int i=0;i<legalPosition.length;i++){
			if(move.getFrom_row()==legalPosition[i][0]&&move.getFrom_col()==legalPosition[i][1]){
				//System.out.println("开始位置合法");
				from_is_in=true;
				break;
			}
		}
		for(int i=0;i<legalPosition.length;i++){
			if(move.getTo_row()==legalPosition[i][0]&&move.getTo_col()==legalPosition[i][1]){
				//System.out.println("结束位置合法");
				to_is_in=true;
				break;
			}
		}
		if(!(from_is_in&&to_is_in))
			return false;
		//另外，“象眼”上不能有棋子
		if(situation.getChess((move.getFrom_row()+move.getTo_row())/2, (move.getFrom_col()+move.getTo_col())/2)!=0)return false;
		return true;
	}
	
	/*
	 * 判断士的行走的合法性
	 */
	private boolean judgeGuardMove(Situation situation, Move move){
		//首先是走斜路的
		if(!(Math.abs(move.getTo_row()-move.getFrom_row())==1&&Math.abs(move.getTo_col()-move.getFrom_col())==1)){
			return false;
		}
		//另外，必须是在九宫之内，这个要分为红方、黑方来分别判断
		if(move.getChess_num()==Situation.A_GUARD){//A方
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
	 * 判断将的行走的合法性
	 */
	private boolean judgeGeneralMove(Situation situation, Move move){
		//“将”有一种特殊的走法：如果两个老将对脸，则可以直接吃掉对方的老将
		if(move.getChess_num()==Situation.A_GENERAL){
			if(situation.getChess(move.getTo_row(), move.getTo_col())==Situation.B_GENERAL){//首先，目标应是对方老将
				if(move.getTo_col()==move.getFrom_col()){//另外，需要走直线
					//统计沿路的棋子数
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
					if(num_chesses==0){//还有，两个老将之间不应有其他棋子
						return true;
					}
				}
			}
		}else if(move.getChess_num()==Situation.B_GENERAL){
			if(situation.getChess(move.getTo_row(), move.getTo_col())==Situation.A_GENERAL){
				if(move.getTo_col()==move.getFrom_col()){
					//统计沿路的棋子数
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
		//首先，必须是只走一格
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
		//另外，必须是在九宫中
		if(move.getChess_num()==Situation.A_GENERAL){//A方
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
	 * 判断炮的行走的合法性
	 */
	private boolean judgeCanonMove(Situation situation, Move move){
		//首先，必须是直线
		if(move.getFrom_col()!=move.getTo_col()&&move.getFrom_row()!=move.getTo_row()){
			return false;
		}
		//另外，看结束位置上是否有棋子（即是否是吃步），分为两种情况处理
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
			//如果没有吃子的话，沿路不能有棋子出现
			if(totalChesses>0)
				return false;
		}else{
			//如果吃子，则沿路有且仅有一个棋子出现
			if(totalChesses!=1){
				return false;
			}
		}
		
		
		return true;
	}
	/*
	 * 判断卒的行走的合法性
	 */
	private boolean judgeInfantryMove(Situation situation, Move move){
		//首先，必须是行走一个格子
		if(move.getFrom_col()==move.getTo_col()){
			if(Math.abs(move.getFrom_row()-move.getTo_row())!=1)return false;
		}else if(move.getFrom_row()==move.getTo_row()){
			if(Math.abs(move.getFrom_col()-move.getTo_col())!=1)return false;
		}else{
			return false;
		}
		
		//卒要分红黑方分别判断
		if(move.getChess_num()==Situation.A_INFANTRY){//A方的卒
			if(move.getTo_row()<move.getFrom_row())//卒不能后退
				return false;
			if(move.getFrom_row()==move.getTo_row()&&move.getFrom_row()<=4)//在过河前不能平移
				return false;
		}else{//B方的卒
			if(move.getTo_row()>move.getFrom_row())
				return false;
			if(move.getFrom_row()==move.getTo_row()&&move.getFrom_row()>=5)
				return false;
		}
	
		return true;
	}
	
}
